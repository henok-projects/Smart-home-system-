package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.apiclient;

import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.GCSAPIClientAuthenticationRequestDTO;
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
public class RemoteGCSAPIClientAuthenticatorService extends GCSAPIClientAuthenticator {

    @Autowired
    GCSRemoteRequests gcsRemoteRequests;

    @Override
    public GCSResponse<? extends AuthenticationResponseDTO> performAuthentication(AuthenticationRequestDTO authenticationRequestDTO) {
        if (!(authenticationRequestDTO instanceof GCSAPIClientAuthenticationRequestDTO gcsapiClientAuthenticationRequestDTO)){
            return GCSResponse.errorResponse(GCSResponseErrorType.MISMATCH_ERROR);
        }
        /*
           Get the api key which is used to check the item in cache
         */
        var apiKey = gcsapiClientAuthenticationRequestDTO.getApiKey();
        var clientName = gcsapiClientAuthenticationRequestDTO.getApiClientDeviceName();
        if (apiKey.isEmpty()) {
            return GCSResponse.errorResponseWithMessage(GCSResponseErrorType.PARSING_ERROR, "Failed to decode token");
        }
        /*
        Get the authentication token, from it we get the service name which is used to check the item in cache
         */
        /*
        Get the CACHE authentication response for this service name and token
        - If the optional is empty, that means the cache doesn't know or isn't sure that it's authenticated
         */
        var cacheAuthResponseOpt = this.getSessionListCacheAuthenticationResponseFor(apiKey, apiKey, GCSResponseErrorType.API_KEY_EXPIRED); // TODO: Make cache catered for api keys
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
                .destination(GCSMicroservice.GCS_SENTRY, "isAuthenticated/apiClient")
                .httpMethod(HttpMethod.POST)
                .setRequestPayload(gcsapiClientAuthenticationRequestDTO)
                .performRequestWithGCSResponse();
        return response.toGCSResponse();
    }

    @Override
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> receiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO) {
        return GCSResponse.errorResponse(GCSResponseErrorType.NOT_IMPLEMENTED);
    }
}
