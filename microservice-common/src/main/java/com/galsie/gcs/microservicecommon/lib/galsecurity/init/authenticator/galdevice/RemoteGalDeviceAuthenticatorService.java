package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.galdevice;

import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GalDeviceAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.GCSRemoteRequests;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class RemoteGalDeviceAuthenticatorService extends GalDeviceAuthenticator {

    @Autowired
    GCSRemoteRequests gcsRemoteRequests;

    public GCSResponse<? extends AuthenticationResponseDTO> performAuthentication(AuthenticationRequestDTO authenticateDeviceRemoteRequestDTO) {
        if (!(authenticateDeviceRemoteRequestDTO instanceof GalDeviceAuthenticationRequestDTO galDeviceAuthenticationRequestDTO)){
            return GCSResponse.errorResponse(GCSResponseErrorType.MISMATCH_ERROR);
        }
        /*
           Get the session token, try decoding it. If that failed, return an error
         */
        var sessionToken = galDeviceAuthenticationRequestDTO.getToken();
        var codableGalDeviceSessionToken = CodableGalDeviceAuthSessionToken.fromStringToken(sessionToken);
        if (codableGalDeviceSessionToken.isEmpty()) {
            return AuthenticationResponseDTO.responseError(GCSResponseErrorType.GALDEVICE_AUTH_TOKEN_INVALID);
        }
        /*
        Get the authentication token, from it we get the serial number which is used to check the item in cache
         */
        var deviceAuthSessionToken = codableGalDeviceSessionToken.get();
        String serialNumber = deviceAuthSessionToken.getSerialNumber();
        /*
        Get the CACHE authentication response for this serial number and token
        - If the optional is empty, that means the cache doesn't know or isn't sure that it's authenticated
         */
        var cacheAuthResponseOpt = this.getSessionListCacheAuthenticationResponseFor(serialNumber, sessionToken, GCSResponseErrorType.GALDEVICE_AUTH_SESSION_EXPIRED);
        if (cacheAuthResponseOpt.isPresent()){
            return cacheAuthResponseOpt.get();
        }
         /*
         In this case, the token was not found in cache, , or it was expired (due to force expire or validity ending)
          So, we request the authentication of this token if:
           - the session in the active session list is invalid (since the validTime may have been extended)
           - A session with the matching token wasn't found in the list

           NOTES:
           - It's up to the local authenticator to send an update over rabbitmq for the sake of updating the cache (it does so)
           - It's up to the implementing service to receive the update over rabbitmq for the sake of updating the cache (and they should do so)
          */
        var response = gcsRemoteRequests
                .initiateRequest(AuthenticationResponseDTO.class)
                .destination(GCSMicroservice.SMART_DEVICES, "authenticate/galdevice")
                .httpMethod(HttpMethod.POST)
                .setRequestPayload(authenticateDeviceRemoteRequestDTO)
                .performRequestWithGCSResponse();
        return response.toGCSResponse();
    }


    @Override
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> receiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO) {
        return GCSResponse.errorResponse(GCSResponseErrorType.NOT_IMPLEMENTED);
    }
}
