package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.apiclient;

import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.GalSecurityAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GCSAPIClientAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public abstract class GCSAPIClientAuthenticator extends GalSecurityAuthenticator {
    static Logger logger = LogManager.getLogger();

    public GalSecurityAuthSessionType galSecurityAuthSessionType(){
        return GalSecurityAuthSessionType.GCS_API_CLIENT;
    }

    @Override
    public Optional<? extends AuthenticationRequestDTO> getAuthenticationRequestDTO(HttpServletRequest httpServletRequest) {
        String apiKey = httpServletRequest.getHeader("gcsApiKey"); // TODO: Load apiKey identifier from configuration
        String apiClientDeviceName = httpServletRequest.getHeader("apiClientDeviceName"); // TODO: Load apiClientDeviceName identifier from configuration
        if (apiKey == null || apiClientDeviceName == null || apiKey.isEmpty() || apiClientDeviceName.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(new GCSAPIClientAuthenticationRequestDTO(apiKey, apiClientDeviceName));
    }

    @Override
    public Optional<? extends GalSecurityAuthSession<?>> createAuthSession(AuthenticationRequestDTO authenticationRequestDTO, AuthenticationResponseDTO authenticationResponseDTO) {
        if (!(authenticationRequestDTO instanceof GCSAPIClientAuthenticationRequestDTO gcsApiClientAuthenticationRequestDTO)){
            return Optional.empty();
        }
        if (!authenticationResponseDTO.isAuthenticated()){
            logger.error("Failed to create authentication session for the API client with key " + gcsApiClientAuthenticationRequestDTO.getApiKey() + " : Not Authenticated");
            return Optional.empty();
        }
        var session = new GCSAPIClientSecurityAuthSession(gcsApiClientAuthenticationRequestDTO.getApiClientDeviceName(), gcsApiClientAuthenticationRequestDTO.getApiKey());
        return Optional.of(session);
    }
}
