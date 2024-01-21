package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.microservice;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.GalSecurityAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GCSMicroserviceAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public abstract class GCSMicroserviceAuthenticator extends GalSecurityAuthenticator {
    static Logger logger = LogManager.getLogger();

    public GalSecurityAuthSessionType galSecurityAuthSessionType(){
        return GalSecurityAuthSessionType.GCS_MICROSERVICE;
    }

    @Override
    public Optional<? extends AuthenticationRequestDTO> getAuthenticationRequestDTO(HttpServletRequest httpServletRequest) {
        String authToken = httpServletRequest.getHeader("microserviceAuthToken"); // TODO: Load microserviceAuthToken identifier from configuration
        if (authToken == null || authToken.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(new GCSMicroserviceAuthenticationRequestDTO(authToken));
    }

    @Override
    public Optional<? extends GalSecurityAuthSession<?>> createAuthSession(AuthenticationRequestDTO authenticationRequestDTO, AuthenticationResponseDTO authenticationResponseDTO) {
        if (!(authenticationRequestDTO instanceof GCSMicroserviceAuthenticationRequestDTO gcsMicroserviceAuthenticationRequestDTO)){
            return Optional.empty();
        }
        if (!authenticationResponseDTO.isAuthenticated()){
            logger.error("Failed to create authentication session for the API client with token " + gcsMicroserviceAuthenticationRequestDTO.getToken() + " : Not Authenticated");
            return Optional.empty();
        }
        var codableAuthSessionTokenOpt = CodableGCSMicroserviceAuthSessionToken.fromStringToken(gcsMicroserviceAuthenticationRequestDTO.getToken());
        if (codableAuthSessionTokenOpt.isEmpty()){ // shouldn't be empty (since otherwise the response wouldn't have return isAuthenticated being true) but just in case
            logger.error("Failed to create authentication session: Couldn't decode the microservice's session token");
            return Optional.empty();
        }
        var codableSessionToken = codableAuthSessionTokenOpt.get();
        var session = new GCSMicroserviceSecurityAuthSession(codableSessionToken.getServiceName(), gcsMicroserviceAuthenticationRequestDTO.getToken());
        return Optional.of(session);
    }
}
