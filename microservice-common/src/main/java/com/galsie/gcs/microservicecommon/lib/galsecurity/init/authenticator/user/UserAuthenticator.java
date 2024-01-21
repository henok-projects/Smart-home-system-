package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user;

/*
UserAuthenticator describes
 */

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.GalSecurityAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.UserAuthenticationRequestDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


public abstract class UserAuthenticator extends GalSecurityAuthenticator {
    static Logger logger = LogManager.getLogger();

    public GalSecurityAuthSessionType galSecurityAuthSessionType(){
        return GalSecurityAuthSessionType.USER;
    }

    public Optional<AuthenticationRequestDTO> getAuthenticationRequestDTO(HttpServletRequest httpServletRequest) {
        String authToken = httpServletRequest.getHeader("userAuthToken"); // TODO: Load token key identifier from configuration
        if (authToken == null){
            return Optional.empty();
        }
        return Optional.of(new UserAuthenticationRequestDTO(authToken));
    }


    public Optional<? extends GalSecurityAuthSession<?>> createAuthSession(AuthenticationRequestDTO authenticationRequestDTO, AuthenticationResponseDTO authenticationResponseDTO){
        if (!(authenticationRequestDTO instanceof UserAuthenticationRequestDTO userAuthRequestDTO)){
            return Optional.empty();
        }
        var userAuthSessionTokenOpt = CodableUserAuthSessionToken.fromStringToken(userAuthRequestDTO.getToken());
        if (userAuthSessionTokenOpt.isEmpty()){
            logger.error("Failed to create authentication session: Couldn't decode the user account session token");
            return Optional.empty();
        }
        var userAuthSessionToken = userAuthSessionTokenOpt.get();

        if (!authenticationResponseDTO.isAuthenticated()){
            logger.error("Failed to create authentication session for the user with id " + userAuthSessionToken.getUserId() + " : Not Authenticated");
            return Optional.empty();
        }
        var session = new UserSecurityAuthSession(userAuthSessionToken.getUserId(), userAuthRequestDTO.getToken());
        return Optional.of(session);
    }

}
