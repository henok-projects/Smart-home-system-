package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user;

import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.UserAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.GCSRemoteRequests;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RemoteUserAuthenticatorService extends UserAuthenticator {

    @Autowired
    GCSRemoteRequests gcsRemoteRequests;

    @Autowired(required = false)
    @Lazy
    GCSRabbitTemplate gcsRabbitTemplate;

    public GCSResponse<? extends AuthenticationResponseDTO> performAuthentication(AuthenticationRequestDTO authenticateUserRemoteRequestDTO) {
        if (!(authenticateUserRemoteRequestDTO instanceof UserAuthenticationRequestDTO requestDTO)) {
            return GCSResponse.errorResponse(GCSResponseErrorType.MISMATCH_ERROR);
        }
        var response = gcsInternalDoPerformAuthentication(requestDTO);
        if (gcsRabbitTemplate == null || response.hasError()){
            return response;
        }
        // MARK: Send last access to the last access queue
        gcsRabbitTemplate.convertAndSend(galSecurityAuthSessionType().getLastAccessCommonQueueType(), new AuthSessionLastAccessUpdateDTO(requestDTO.getToken(), LocalDateTime.now()));
        return response;
    }

    public GCSResponse<? extends AuthenticationResponseDTO> gcsInternalDoPerformAuthentication(UserAuthenticationRequestDTO requestDTO) {

        /*
           Get the session token, try decoding it. If that failed, return an error
         */
        var sessionToken = requestDTO.getToken();
        var userAccountAuthSessionTokenOpt = CodableUserAuthSessionToken.fromStringToken(sessionToken);
        if (userAccountAuthSessionTokenOpt.isEmpty()) {
            return AuthenticationResponseDTO.responseError(GCSResponseErrorType.USER_AUTH_TOKEN_INVALID);
        }
        /*
        Get the user authentication token, from it get the user id which is used to check the item in cache
         */
        var userAuthSessionToken = userAccountAuthSessionTokenOpt.get();
        long userId = userAuthSessionToken.getUserId();
        /*
        Get the CACHE authentication response for this user id and token
        - If the optional is empty, that means the cache doesn't know or isn't sure that it's authenticated
         */
        var cacheAuthResponseOpt = this.getSessionListCacheAuthenticationResponseFor(userId, sessionToken, GCSResponseErrorType.USER_AUTH_SESSION_EXPIRED);
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
                .destination(GCSMicroservice.USERS, "authentication/authenticateUser")
                .httpMethod(HttpMethod.POST)
                .setRequestPayload(requestDTO)
                .performRequestWithGCSResponse();
        return response.toGCSResponse();
    }

    @Override
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> receiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO) {
        return GCSResponse.errorResponse(GCSResponseErrorType.NOT_IMPLEMENTED);
    }
}
