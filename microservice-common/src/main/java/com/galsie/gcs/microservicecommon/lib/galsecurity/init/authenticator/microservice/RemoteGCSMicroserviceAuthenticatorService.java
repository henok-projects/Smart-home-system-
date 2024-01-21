package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.microservice;

import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GCSMicroserviceAuthenticationRequestDTO;
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
public class RemoteGCSMicroserviceAuthenticatorService extends GCSMicroserviceAuthenticator{

    @Autowired
    GCSRemoteRequests gcsRemoteRequests;

    @Override
    public GCSResponse<? extends AuthenticationResponseDTO> performAuthentication(AuthenticationRequestDTO authenticationRequestDTO) {
        if (!(authenticationRequestDTO instanceof GCSMicroserviceAuthenticationRequestDTO gcsMicroserviceAuthenticationRequestDTO)){
            return GCSResponse.errorResponse(GCSResponseErrorType.MISMATCH_ERROR);
        }
        /*
           Get the session token, try decoding it. If that failed, return an error
         */
        var sessionToken = gcsMicroserviceAuthenticationRequestDTO.getToken();
        var codableMicroserviceAuthSessionToken = CodableGCSMicroserviceAuthSessionToken.fromStringToken(sessionToken);
        if (codableMicroserviceAuthSessionToken.isEmpty()) {
            return AuthenticationResponseDTO.responseError(GCSResponseErrorType.MICROSERVICE_AUTH_TOKEN_INVALID);
        }
        /*
        Get the authentication token, from it we get the service name which is used to check the item in cache
         */
        var deviceAuthSessionToken = codableMicroserviceAuthSessionToken.get();
        String serviceName = deviceAuthSessionToken.getServiceName();
        /*
        Get the CACHE authentication response for this service name and token
        - If the optional is empty, that means the cache doesn't know or isn't sure that it's authenticated
         */
        var cacheAuthResponseOpt = this.getSessionListCacheAuthenticationResponseFor(serviceName, sessionToken, GCSResponseErrorType.MICROSERVICE_AUTH_SESSION_EXPIRED);
        if (cacheAuthResponseOpt.isPresent()){
            return cacheAuthResponseOpt.get();
        }
         /*
         In this case, the token was not found in cache, or it was expired (due to force expire or validity ending)
          So, we request the authentication of this token if:
           - the session in the active session list is invalid (since the validTime may have been extended)
           - A session with the matching token wasn't found in the list

           NOTES:
           - It's up to the local authenticator to send an update over rabbitmq for the sake of updating the cache (it does so)
           - It's up to the implementing service to receive the update over rabbitmq for the sake of updating the cache (and they should do so)
          */
        var response = gcsRemoteRequests
                .initiateRequest(AuthenticationResponseDTO.class)
                .destination(GCSMicroservice.GCS_SENTRY, "isAuthenticated/microservice")
                .httpMethod(HttpMethod.POST)
                .setRequestPayload(gcsMicroserviceAuthenticationRequestDTO)
                .performRequestWithGCSResponse();
        return response.toGCSResponse();
    }

    @Override
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> receiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO) {
        return GCSResponse.errorResponse(GCSResponseErrorType.NOT_IMPLEMENTED);
    }
}
