package com.galsie.gcs.microservicecommon.lib.gcsrequests;

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceAuthenticationCredentialsInfo;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityContextProvider;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.login.GCSMicroserviceSentryLoginService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.data.GCSRemoteRequestWithGCSResponseInfo;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.RemoteRequestProtocolType;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroserviceDestination;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.GCSRemoteRequestDestination;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.url.GCSSomeUrlDestination;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.data.GCSRemoteRequestResponseInfo;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.data.discrete.GCSRemoteRequestErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.tyref.GCSResponseDTOTypeRef;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseDTO;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

/**
 * GCSRemoteRequest allows initiating remote requests to 2 destinations:
 * - GCSMicroservices through {@link GCSMicroserviceDestination}
 * - Normal urls through {@link GCSSomeUrlDestination}
 * <p>
 * It allows setting:
 * - The Http Method
 * - Header parameters
 * - Header parameters for different {@link GalSecurityAuthSessionType}
 * - Query parameters (x=1&y=2)
 * - The request payload
 * - The response body type
 * <p>
 * Additionally:
 * - The {@link RestTemplate} must be added (currently done by {@link GCSRemoteRequests}} for performing requests
 * - An reference to {@link GCSMicroserviceAuthenticationCredentialsInfo} & {@link GCSMicroserviceSentryLoginService} can be added
 * - Note: only matter if the destionation is a {@link GCSMicroservice}
 * - If {@link GCSMicroserviceAuthenticationCredentialsInfo} is provided, microservice auth token is automatically attached to the header
 * - If {@link GCSMicroserviceSentryLoginService} is provided, and the microservice authentication fails, login is retried
 * <p>
 * The {@link RestTemplate} is used to perform requests through 2 methods:
 * - {@link GCSRemoteRequest#performRequest()}: Performs the request expecting the response body type to be returned as is
 * - {@link GCSRemoteRequest#performRequestWithGCSResponse()}: Performs the request expecting the response body type to be returned wrapped with a {@link GCSResponseDTO}
 * - Note: This is generally the case when the destination is a {@link GCSMicroservice}
 *
 * @param <T> The response body type
 */
@Slf4j
public class GCSRemoteRequest<T> {

    private HashMap<String, String> httpHeaders = new HashMap<>();
    private HashMap<String, String> queryParams = new HashMap<>();

    @NotNull
    WebClient.Builder webClientBuilder;

    @Nullable
    private GCSMicroserviceSentryLoginService gcsMicroserviceSentryLoginService; // set by GCSRemoteRequest if sentry auto login is enabled in GalSecurityConfiguration (so that if microservice authentication fails, login is attempted)

    @Nullable
    GCSMicroserviceAuthenticationCredentialsInfo authenticationCredentialsInfo; // set by GCSRemoteRequest so that if the destination is a GCS microservice, the auth token is automatically added to the header

    @NotNull
    private GCSRemoteRequestDestination gcsRemoteRequestDestination;

    @NotNull
    private HttpMethod httpMethod;

    @Nullable
    private Object requestPayload;

    @NotNull
    private Class<T> responseBodyType;

    @NotNull
    private int maxInMemorySizeInBytes = 300*1024; // payload & response can't exceed this size

    public GCSRemoteRequest<T> setWebClientBuilder(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        return this;
    }

    public GCSRemoteRequest<T> setGCSMicroserviceSentryLoginService(GCSMicroserviceSentryLoginService gcsMicroserviceSentryLoginService) {
        this.gcsMicroserviceSentryLoginService = gcsMicroserviceSentryLoginService;
        return this;
    }

    public GCSRemoteRequest<T> setGCSMicroserviceAuthCredentialsInfo(GCSMicroserviceAuthenticationCredentialsInfo authCredentialsInfo) {
        this.authenticationCredentialsInfo = authCredentialsInfo;
        return this;
    }

    /*
    HTTP Method
     */
    public GCSRemoteRequest<T> httpMethod(HttpMethod method) {
        this.httpMethod = method;
        return this;
    }
    /*
     * Destination
     */

    public GCSRemoteRequest<T> destination(RemoteRequestProtocolType remoteRequestProtocolType, String domain, String... paths) {
        this.gcsRemoteRequestDestination = new GCSSomeUrlDestination(remoteRequestProtocolType, domain, paths);
        return this;
    }

    public GCSRemoteRequest<T> destination(GCSMicroservice microservice, String... paths) {
        this.gcsRemoteRequestDestination = new GCSMicroserviceDestination(microservice, paths);
        return this;
    }

    /*
    Request Headers
     */
    public GCSRemoteRequest<T> addRequestHeaderItem(String key, String value) {
        httpHeaders.put(key, value);
        return this;
    }

    /**
     * Sets for each parameter of {@link GalSecurityAuthSessionType#getTokenHeaderParams()} the value specified in values
     * - The order of values matter and must match the order of the parameters
     * - Generally, all values must be specified. No error is logged, whether the authenticator accepts it or not is up to the authenticator
     */
    public GCSRemoteRequest<T> addRequestHeaderSessionItems(GalSecurityAuthSessionType galSecurityAuthSessionType, String... values) {
        var tokenParams = galSecurityAuthSessionType.getTokenHeaderParams();
        for (int i = 0; i < Math.min(tokenParams.length, values.length); i++) {
            this.addRequestHeaderItem(tokenParams[i], values[i]);
        }
        return this;
    }

    /**
     * For each passed {@link GalSecurityAuthSessionType} that exists in the security context (checked through {@link GalSecurityContextProvider}:
     * - Adds to the request header the session header parameters (gotten through {@link GalSecurityAuthSession#getHeaderParameters()}
     * Note:
     * - If the {@link com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionGroup} isn't set, nothing happens
     * - If the passed {@link GalSecurityAuthSessionType} isn't part of the group, nothing happens
     *
     * @param galSecurityAuthSessionTypes The session types for which we need to add the header parameters to the request header
     */
    public GCSRemoteRequest<T> addRequestHeaderSessionItemsFromGalSecurityContext(GalSecurityAuthSessionType... galSecurityAuthSessionTypes) {
        var authSessionsGroupOpt = GalSecurityContextProvider.getContextualAuthenticationSessionGroup();
        if (authSessionsGroupOpt.isEmpty()) {
            return this;
        }
        var authSessionsGroup = authSessionsGroupOpt.get();
        for (GalSecurityAuthSessionType galSecurityAuthSessionType : galSecurityAuthSessionTypes) {
            var authSessionOpt = authSessionsGroup.getGalSecurityAuthSessionFor(galSecurityAuthSessionType);
            if (authSessionOpt.isEmpty()) {
                continue;
            }
            var authSession = authSessionOpt.get();
            authSession.getHeaderParameters().forEach((key, value) -> this.addRequestHeaderItem(key, value));
        }
        return this;
    }

    /*
    Get Parameters
     */
    public GCSRemoteRequest<T> addQueryParameter(String key, String value) {
        this.queryParams.put(key, value);
        return this;
    }

    /*
    Body
     */
    public GCSRemoteRequest<T> setRequestPayload(Object payload) {
        this.requestPayload = payload;
        return this;
    }

    public GCSRemoteRequest<T> setResponseBodyType(Class<T> responseBodyType) {
        this.responseBodyType = responseBodyType;
        return this;
    }

    public GCSRemoteRequest<T> setMaxInMemorySize(int sizeInBytes) {
        this.maxInMemorySizeInBytes = sizeInBytes;
        return this;
    }

    private boolean isDestinationAGCSMicroservice() {
        return this.gcsRemoteRequestDestination instanceof GCSMicroserviceDestination;
    }

    /**
     * Add the token info if the destination is a GCSMicroserviceDestination
     */
    private void addMicroserviceTokenInfo() {
        if (!this.isDestinationAGCSMicroservice()) {
            return;
        }
        if (this.authenticationCredentialsInfo == null) {
            return;
        }
        // add the gcs microservice token
        this.authenticationCredentialsInfo.getMicroserviceAuthToken().ifPresent(token -> this.addRequestHeaderSessionItems(GalSecurityAuthSessionType.GCS_MICROSERVICE, token));
    }


    private WebClient.RequestBodySpec getRequestBodySpec(){
        var exchangeStrategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(this.maxInMemorySizeInBytes))
                .build();

        var webClient = webClientBuilder.exchangeStrategies(exchangeStrategies).build();
        var destination = gcsRemoteRequestDestination.getDestinationUri();

        var request = webClient.method(this.httpMethod)
                .uri(destination);
        if (this.requestPayload != null) {
            request.body(BodyInserters.fromValue(this.requestPayload));
        }
        this.httpHeaders.forEach(request::header);
        return request;
    }
    /**
     * Perform the request knowing that the response body type will be returned as is (as opposed to wrapped with a GCSResponse)
     * - Adds the Microservice token info through {@link GCSRemoteRequest#addMicroserviceTokenInfo()} (if the destination is a GCSMicroservice)
     */
    public GCSRemoteRequestResponseInfo<T> performRequest() {
        assert this.gcsRemoteRequestDestination != null;
        assert this.httpMethod != null;
        assert this.responseBodyType != null;
        this.addMicroserviceTokenInfo();

        try {
            var mono = getRequestBodySpec()
                    .retrieve()
                    .toEntity(this.responseBodyType);

            var responseEntity = mono.block();
            return GCSRemoteRequestResponseInfo.forResponseEntity(responseEntity);
        } catch (RestClientException ex) {
            log.error("RestClientException: ", ex);
            return GCSRemoteRequestResponseInfo.error(GCSRemoteRequestErrorType.REST_CLIENT_EXCEPTION);
        }
    }

    /**
     * Performs the request knowing that the responseBodyType will be returned wrapped by a {@link GCSResponseDTO}
     * - Uses {@link GCSRemoteRequest#performRequestWithGCSResponse(int authenticationRetries)} with 2 retries
     */
    public GCSRemoteRequestWithGCSResponseInfo<T> performRequestWithGCSResponse() {
        return performRequestWithGCSResponse(2);
    }

    /**
     * Performs the request knowing that the responseBodyType will be returned wrapped by a {@link GCSResponseDTO}
     * - If the destination is a microservice, the {@link GCSRemoteRequest#authenticationCredentialsInfo} and the {@link GCSRemoteRequest#gcsMicroserviceSentryLoginService} are not null:
     * - When the response is received, the method {@link GCSRemoteRequest#checkIfMicroserviceSessionExpired(GCSRemoteRequestWithGCSResponseInfo)} is called to check if the response indicates that this microservice session expired
     * - If that was the case, uses the login service tries to login with gcs-sentry. The total number of login tries is passed as a parameter to this method
     * - Otherwise, the response is returned as is
     *
     * @param authenticationRetries The total number of login tries if authentication fails
     */
    public GCSRemoteRequestWithGCSResponseInfo<T> performRequestWithGCSResponse(int authenticationRetries) {
        if (!isDestinationAGCSMicroservice() || authenticationCredentialsInfo == null || gcsMicroserviceSentryLoginService == null) {
            return auxPerformRequestWithGCSResponse();
        }
        GCSRemoteRequestWithGCSResponseInfo<T> responseInfo = null;
        while (authenticationRetries > 0) {
            responseInfo = auxPerformRequestWithGCSResponse();
            if (!checkIfMicroserviceSessionExpired(responseInfo)) { // if the login service is null or the microservice session hasn't expired, return the response as as
                return responseInfo;
            }
            try {
                gcsMicroserviceSentryLoginService.login();
            } catch (Exception e) {
                log.info("Failed to login with gcs sentry: " + e.getLocalizedMessage());
            }
            authenticationRetries--;
        }
        return responseInfo;
    }

    /**
     * Auxiliary method that performs the request knowing that the responseBodyType will be returned wrapped by a {@link GCSResponseDTO}
     * - Adds the Microservice token info through {@link GCSRemoteRequest#addMicroserviceTokenInfo()} (if the destination is a GCSMicroservice)
     *
     * @return {@link GCSRemoteRequestWithGCSResponseInfo<T>} holding a {@link GCSRemoteRequestErrorType} or the responseBodyType 'T'
     */
    private GCSRemoteRequestWithGCSResponseInfo<T> auxPerformRequestWithGCSResponse() {

        assert this.gcsRemoteRequestDestination != null;
        assert this.httpMethod != null;
        assert this.responseBodyType != null;
        // add the gcs microservice token
        this.addMicroserviceTokenInfo();
        try {
            var mono = getRequestBodySpec().retrieve()
                    .toEntity(new GCSResponseDTOTypeRef<>(responseBodyType));

            var responseEntity = mono.block();
            return GCSRemoteRequestWithGCSResponseInfo.forResponseEntity(responseEntity);
        } catch (RestClientException ex) {
            log.info("RestClientException: ", ex);
            return GCSRemoteRequestWithGCSResponseInfo.error(GCSRemoteRequestErrorType.REST_CLIENT_EXCEPTION);
        }
    }

    /**
     * Checks if a response holds information indicating the authentication session for this microservice has expired. This is since:
     * - When authentication fails, the {@link GCSResponseDTO} with the error type being authentication related
     * - If the {@link GCSResponseErrorType} authentication error was microservice related, then we consider the session expired
     *
     * @param responseInfo The {@link GCSRemoteRequestWithGCSResponseInfo<T>} from which we can check if the authentication sessiojn expired
     */
    private boolean checkIfMicroserviceSessionExpired(GCSRemoteRequestWithGCSResponseInfo<T> responseInfo) {
        if (responseInfo.hasError()) {
            return false;
        }
        var gcsResponse = responseInfo.getResponseEntity().getBody();
        if (!gcsResponse.hasError()) {
            return false;
        }
        var error = gcsResponse.getError().getErrorType();
        if (!error.isAuthRelated()) {
            return false;
        }
        return error == GCSResponseErrorType.MICROSERVICE_AUTH_SESSION_EXPIRED || error == GCSResponseErrorType.MICROSERVICE_AUTH_TOKEN_INVALID;
    }
}
