package com.galsie.gcs.users.configuration.security.contexthelper;


import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityContextProvider;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.UserSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.users.repository.UserAuthSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType.AUTH_SESSION_NOT_FOUND;

@Component
@Slf4j
public class UserGalSecurityContextHelper {

    @Autowired
    UserAuthSessionRepository userAuthSessionRepository;

    /**
     * Gets the authenticated user information from the security context, may fail.
     *
     * @return
     * @throws GCSResponseException If authentication failed
     */

    public UserSecurityAuthSession getUserAuthSession() throws GCSResponseException {
        var userAuthSessionOpt = GalSecurityContextProvider.getContextualUserAuthSession();
        if (userAuthSessionOpt.isEmpty()) {
            throw new GCSResponseException(AUTH_SESSION_NOT_FOUND);
        }
        return userAuthSessionOpt.get();
    }

    public ContextualAuthenticatedUserInfo getAuthenticatedUserInfo() throws GCSResponseException {
        var userSecurityAuthSessionOpt = GalSecurityContextProvider.getContextualUserAuthSession();
        if (userSecurityAuthSessionOpt.isEmpty()) { // It shouldN't be empty due to the request filter
            throw new GCSResponseException(GCSResponseErrorType.AUTH_SESSION_NOT_FOUND);
        }
        var userSecurityAuthSession = userSecurityAuthSessionOpt.get();
        var authToken = userSecurityAuthSession.getToken();

        var userAuthSessionEntityOpt = userAuthSessionRepository.findBySessionToken(authToken);
        if (userAuthSessionEntityOpt.isEmpty()) {
            // WHY would the session not be found when the security context had the session? Log an issue
            log.error("Error while getting the authenticated user info for the user " + userSecurityAuthSession.getUserId() + ": Authentication session entity was not found; however, a session exists in context..");
            throw new GCSResponseException(GCSResponseErrorType.AUTH_SESSION_NOT_FOUND);
        }
        var userAuthSessionEntity = userAuthSessionEntityOpt.get();
        return new ContextualAuthenticatedUserInfo(userAuthSessionEntity);
    }


}
