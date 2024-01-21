package com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters;

import com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig.AuthSessionTypeConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityContextProvider;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.AuthenticatedGCSRequestAutoConfPathsProvider;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequestObject;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticationStrategy;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionGroup;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 *This class functions as the main request filter for GalSecurity. it is responsible for checking if a request requires authentication
 * for all configured session types done in the class that implements {@link com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration}
 */
@Slf4j
public class CustomGalSecurityRequestFilter implements Filter {


    protected final Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> sessionTypeConfigurations;


    private final AuthenticatedGCSRequestAutoConfPathsProvider authenticatedGCSRequestAutoConfPathsProvider;

    protected final MappingJackson2HttpMessageConverter messageConverter;

    public CustomGalSecurityRequestFilter(Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> authenticatorServices, AuthenticatedGCSRequestAutoConfPathsProvider authenticatedGCSRequestAutoConfPathsProvider) {
        this.sessionTypeConfigurations = authenticatorServices;
        this.messageConverter = new MappingJackson2HttpMessageConverter();
        this.authenticatedGCSRequestAutoConfPathsProvider = authenticatedGCSRequestAutoConfPathsProvider;
    }

    /**
     * Checks if the uri requires authentication. it first checks if the uri is a key in {@link AuthenticatedGCSRequestAutoConfPathsProvider#getPathsToAuthenticatedGCSRequestObjectMap()}
     * before checking {@link AuthSessionTypeConfiguration#getManualConfiguration()} for each {@link AuthSessionTypeConfiguration}
     * in {@link CustomGalSecurityRequestFilter#sessionTypeConfigurations}
     * @param uri the uri to check
     * @return boolean indicating whether the uri requires authentication
     */
    private boolean doesUriRequireAuth(String uri){
        if (authenticatedGCSRequestAutoConfPathsProvider == null){
            return true; // IF for some reason its NULL (which it shouldn't be), assume all paths require authentication for security reasons
        }
        if (uri.isEmpty() || uri.charAt(0) != '/' ){
            uri = "/" + uri;
        }
        if(authenticatedGCSRequestAutoConfPathsProvider.getPathsToAuthenticatedGCSRequestObjectMap().containsKey(uri)){
            return true;
        }
        String finalUri = uri;
        return sessionTypeConfigurations.values().stream().filter(s-> s.getManualConfiguration()!= null).anyMatch(s-> s.getManualConfiguration().doesUriRequireAuth(finalUri));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) servletRequest;
        var httpServletResponse = (HttpServletResponse) servletResponse;

        String uri = httpServletRequest.getRequestURI(); // TODO: PERHAPS MAKE MORE SECURE

        if (!this.doesUriRequireAuth(uri)) { // only require requests that DONT require authentication to be authenticated (uris that require authentication are stored in each services configu)
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        var authenticatedGCSRequests = getAllAuthenticatedGCSRequests(uri);
        if (authenticatedGCSRequests.size() == 0){
            // This should never be true, so why did it pass?
            this.writeGCSResponseToHTTPServlet(GCSResponse.errorResponse(GCSResponseErrorType.INTERNAL_AUTHENTICATION_CONFIGURATION_ERROR), httpServletResponse);
        }
        /**
         * Since we are doing AND and OR authentication, we may be trying to authenticate multiple sessions. Which error do we return???
         * ---> We return the first non-missing credentials authentication error.
         * ---> UNLESS there was an AUTHENTICATION_SESSION_CREATION_ERROR, in that case we consider it a high priority and we return it - since auth succeeded, but a gcs issue prevented session creation
         * ---> If all are missing credentials, we return missing credentials
         */

        GCSResponse<?> firstNonMissingCredentialsError = null;
        for (AuthenticatedGCSRequestObject authenticatedGCSRequest : authenticatedGCSRequests) {//between AuthenticatedGCSRequestObjects it is an implied OR authentication strategy
            GCSResponse<?> filterMethodResponse;
            if (authenticatedGCSRequest.getAuthenticationStrategy() == AuthenticationStrategy.AND) {
                filterMethodResponse = doFilterAnd(httpServletRequest, authenticatedGCSRequest);
            } else {
                filterMethodResponse = doFilterOr(httpServletRequest, authenticatedGCSRequest);
            }
            // because of checks in doFilterAnd and doFilterOr, we know that if the response has an error, it is because the authentication failed that authentication check
            // we attempt the next AuthenticatedGCSRequestObject
            // if the response does not have an error, it means that the authentication passed then we can continue to the next filter
            if (!filterMethodResponse.hasError()) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            var filterMethodResponseErrorType = filterMethodResponse.getGcsError().getErrorType();
            log.warn("Authentication failed for " + Arrays.toString(authenticatedGCSRequest.getAuthSessionTypes()) + " " + authenticatedGCSRequest.getAuthenticationStrategy() + " reason: " + filterMethodResponseErrorType);

            if (filterMethodResponseErrorType == GCSResponseErrorType.AUTHENTICATION_SESSION_CREATION_ERROR){
                // Takes priority, because auth suceeded, but session failed, so we set it
                firstNonMissingCredentialsError = filterMethodResponse;
            }else if (filterMethodResponseErrorType != GCSResponseErrorType.MISSING_AUTHENTICATION_CREDENTIALS && firstNonMissingCredentialsError == null){
                firstNonMissingCredentialsError = filterMethodResponse;
            }
        }
        log.error("Authentication failed");
        if (firstNonMissingCredentialsError == null) {
            firstNonMissingCredentialsError = GCSResponse.errorResponse(GCSResponseErrorType.MISSING_AUTHENTICATION_CREDENTIALS);
        }
        this.writeGCSResponseToHTTPServlet(firstNonMissingCredentialsError, httpServletResponse);
    }

    private <T> void writeGCSResponseToHTTPServlet(GCSResponse<?> gcsResponse, HttpServletResponse response) throws IOException {
        var responseEntity  = gcsResponse.toResponseEntity();

        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        // set status
        response.setStatus(responseEntity.getStatusCodeValue());

        // copy headers
        outputMessage.getHeaders().putAll(responseEntity.getHeaders());

        // write body
        if (responseEntity.hasBody()) {
            messageConverter.write(responseEntity.getBody(), null, outputMessage);
        }
    }


    /**
     * Add the session to the {@link GalSecurityAuthSessionGroup} in the {@link GalSecurityContextProvider#getSecurityContext()}
     * @param session
     * @throws ServletException
     */
    private void addSessionToSessionGroup(GalSecurityAuthSession<?> session) throws ServletException {
        var authentication = GalSecurityContextProvider.getSecurityContext().getAuthentication();
        if (authentication == null) {
            authentication = new GalSecurityAuthSessionGroup();
            GalSecurityContextProvider.getSecurityContext().setAuthentication(authentication);
        } else if (!(authentication instanceof GalSecurityAuthSessionGroup)) {
            throw new ServletException("Authentication must be an instance of GalSecurityAuthSessionGroup");
        }
        var authSessGroup = (GalSecurityAuthSessionGroup) authentication;
        authSessGroup.putGalSecurityAuthSession(session);
    }


    /**
     * Perform authentication for all the session types specified in the {@link AuthenticatedGCSRequestObject#getAuthSessionTypes()}. The
     * method loops the {@link AuthenticatedGCSRequestObject#getAuthSessionTypes()} and perform authentication, if any authentication
     * is not passed, the methods return a false and the response entity explaining the cause of the failure.
     * if all authentication methods are passed, the method returns true and null.
     * @param request HttpServletRequest were authentication header information is read from
     * @param authenticatedGCSRequestObject the {@link AuthenticatedGCSRequestObject} annotation that specifies the authentication methods to use
     * @return Pair<Boolean, ResponseEntity<?>> where the first value is true if the authentication passed and false if it failed. The second value is the response entity to return if the authentication failed
     */
    private GCSResponse<?> doFilterAnd(HttpServletRequest request, AuthenticatedGCSRequestObject authenticatedGCSRequestObject) throws ServletException {
        var sessionTypes = authenticatedGCSRequestObject.getAuthSessionTypes();
        for(var sessionType: sessionTypes){
            var authSessionTypeConfiguration = this.sessionTypeConfigurations.get(sessionType);
            var authenticator = authSessionTypeConfiguration.getAuthenticator();
            var authRequestOpt = authenticator.getAuthenticationRequestDTO(request);
            log.info("Auth request: " + authRequestOpt);
            if (authRequestOpt.isEmpty()) {
                return GCSResponse.errorResponse(GCSResponseErrorType.MISSING_AUTHENTICATION_CREDENTIALS);
            }
            var authRequestDTO = authRequestOpt.get();
            var response = authenticator.performAuthentication(authRequestOpt.get());
            log.info("Auth response: " + response.getResponseData());
            if (response.hasError()) {
                return response;
            }
            var authResponseDTO = response.getResponseData();
            log.info("Auth response data: " + authResponseDTO.isAuthenticated());
            if (!authResponseDTO.isAuthenticated()) {
                return GCSResponse.errorResponse(authResponseDTO.getAuthenticationResponseError());
            }
            var sessionOpt = authenticator.createAuthSession(authRequestDTO, authResponseDTO);
            if (sessionOpt.isEmpty()) {
                return GCSResponse.errorResponse(GCSResponseErrorType.AUTHENTICATION_SESSION_CREATION_ERROR);
            }
            log.info("Auth session: " + sessionOpt.get());
            this.addSessionToSessionGroup(sessionOpt.get());
        }
        return GCSResponse.response(true);
    }

    /**
     * Perform authentication for all the session types specified in the {@link AuthenticatedGCSRequestObject#getAuthSessionTypes()} annotation. The
     *
     * The method loops through  {@link AuthenticatedGCSRequestObject#getAuthSessionTypes()} and perform authentication:
     * - if any authentication is not passed, we log the error and move to next iteration of the loop.
     * - IF all authentication fails the methods:
     * ---> We return the first non-missing credentials authentication error.
     * ---> UNLESS there was an AUTHENTICATION_SESSION_CREATION_ERROR, in that case we return it - since its more important than other errors.
     * ---> If all are missing credentials, we return missing credentials
     * - IF any authentication passes, the method returns true and null.
     * @param request HttpServletRequest were authentication header information is read from
     * @param authenticatedGCSRequestObject the {@link AuthenticatedGCSRequestObject} that specifies the authentication methods to use
     * @return GCSResponse<?> where not having an error indicates that the authentication passed
     */
    private GCSResponse<?> doFilterOr( HttpServletRequest request,AuthenticatedGCSRequestObject authenticatedGCSRequestObject) throws ServletException {
        var sessionTypes = authenticatedGCSRequestObject.getAuthSessionTypes();
        GCSResponseErrorType firstNonMissingCredentialsError = null;
        for(var sessionType: sessionTypes){
            var authSessionTypeConfiguration = this.sessionTypeConfigurations.get(sessionType);
            var authenticator = authSessionTypeConfiguration.getAuthenticator();
            var authRequestOpt = authenticator.getAuthenticationRequestDTO(request);
            if (authRequestOpt.isEmpty()) {
                // MISSING AUTH CREDENTIALS
                //log.error("Missing authentication credentials " + sessionType);
                continue;
            }
            var authRequestDTO = authRequestOpt.get();
            var response = authenticator.performAuthentication(authRequestOpt.get());
            if (response.hasError()) { // GCS error while performing authentication
                // Since we return the first non-missing credentials error, we only set it if it were null
                firstNonMissingCredentialsError = firstNonMissingCredentialsError == null ? response.getGcsError().getErrorType(): firstNonMissingCredentialsError;
                continue;
            }
            var authResponseDTO = response.getResponseData();
            if (!authResponseDTO.isAuthenticated()) {
                //log.error(GCSResponseErrorType.AUTHENTICATION_FAILED.toString(), authResponseDTO);
                // Since we return the first non-missing credentials error, we only set it if it were null
                firstNonMissingCredentialsError = firstNonMissingCredentialsError == null ? authResponseDTO.getAuthenticationResponseError(): firstNonMissingCredentialsError;
                continue;
            }
            var sessionOpt = authenticator.createAuthSession(authRequestDTO, authResponseDTO);
            if (sessionOpt.isEmpty()) {
                // NOTE: This takes priority over firstNonMissingCredentialsError, as the session simply failed to create, but auth suceeded
                firstNonMissingCredentialsError = GCSResponseErrorType.AUTHENTICATION_SESSION_CREATION_ERROR;
                //log.error(GCSResponseErrorType.AUTHENTICATION_SESSION_CREATION_ERROR.toString());
                continue;
            }
            this.addSessionToSessionGroup(sessionOpt.get());
            return GCSResponse.response(true);
        }
        return GCSResponse.errorResponse(firstNonMissingCredentialsError == null ? GCSResponseErrorType.MISSING_AUTHENTICATION_CREDENTIALS: firstNonMissingCredentialsError);
    }

    /**
     * This method gets the {@link AuthenticatedGCSRequestObject} for a given uri if it is manually configured
     * @param uri
     * @return
     */
    private Optional<AuthenticatedGCSRequestObject> getManuallyAuthenticatedGCSRequestObject(String uri){
        Set<GalSecurityAuthSessionType>  matchingSecurityAuthSessionSet = new HashSet<>();
        for(var entry: sessionTypeConfigurations.entrySet()){
            var authSessionType = entry.getKey();
            if(entry.getValue().getManualConfiguration() != null && entry.getValue().getManualConfiguration().doesUriRequireAuth(uri)){
                matchingSecurityAuthSessionSet.add(authSessionType);
            }
        }
        if(matchingSecurityAuthSessionSet.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(new AuthenticatedGCSRequestObject(AuthenticationStrategy.AND, matchingSecurityAuthSessionSet.toArray(new GalSecurityAuthSessionType[0])));
    }

    /**
     * This method get all auto and manually configured {@link AuthenticatedGCSRequestObject} for a given uri
     */
    private List<AuthenticatedGCSRequestObject> getAllAuthenticatedGCSRequests(String uri){
        var authenticatedGCSRequestObjects = authenticatedGCSRequestAutoConfPathsProvider.getPathsToAuthenticatedGCSRequestObjectMap().get(uri);
        var authenticatedGCSRequestObjectOpt = getManuallyAuthenticatedGCSRequestObject(uri);
        authenticatedGCSRequestObjectOpt.ifPresent(authenticatedGCSRequestObjects::add);
        return authenticatedGCSRequestObjects;
    }

}
