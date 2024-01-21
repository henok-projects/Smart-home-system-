package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.galdevice;

import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.GalSecurityAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GalDeviceAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public abstract class GalDeviceAuthenticator extends GalSecurityAuthenticator {
    static Logger logger = LogManager.getLogger();

    public GalSecurityAuthSessionType galSecurityAuthSessionType(){
        return GalSecurityAuthSessionType.GALDEVICE;
    }

    public Optional<AuthenticationRequestDTO> getAuthenticationRequestDTO(HttpServletRequest httpServletRequest) {
        String authToken = httpServletRequest.getHeader("authToken");
        if (authToken == null){
            return Optional.empty();
        }
        return Optional.of(new GalDeviceAuthenticationRequestDTO(authToken));
    }

    public Optional<? extends GalSecurityAuthSession<?>> createAuthSession(AuthenticationRequestDTO authenticationRequestDTO, AuthenticationResponseDTO authenticationResponseDTO){
        if (!(authenticationRequestDTO instanceof GalDeviceAuthenticationRequestDTO galDeviceAuthRequestDTO)){
            return Optional.empty();
        }
         /*
           Get the session token, try decoding it. If that failed, return an error
         */
        var galDeviceAuthSessionTokenOpt = CodableGalDeviceAuthSessionToken.fromStringToken(galDeviceAuthRequestDTO.getToken());
        if (galDeviceAuthSessionTokenOpt.isEmpty()){
            logger.error("Failed to create authentication session: Couldn't decode the galdevice session token");
            return Optional.empty();
        }
        var galDeviceAuthSessionToken = galDeviceAuthSessionTokenOpt.get();
        if (!authenticationResponseDTO.isAuthenticated()){
            logger.error("Failed to create authentication session for the device with the with the serial number " + galDeviceAuthSessionToken.getSerialNumber() + " : Not Authenticated");
            return Optional.empty();
        }
        var session = new GalDeviceSecurityAuthSession(galDeviceAuthSessionToken.getSerialNumber(), galDeviceAuthRequestDTO.getToken());
        return Optional.of(session);
    }

}
