package com.galsie.gcs.microservicecommon.lib.galsecurity.init.login;

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceAuthenticationCredentialsInfo;
import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceGeneralLocalProperties;
import com.galsie.gcs.microservicecommon.data.dto.microservice.login.request.GCSMicroserviceLoginRequestDTO;
import com.galsie.gcs.microservicecommon.data.dto.microservice.login.response.GCSMicroserviceLoginResponseDTO;
import com.galsie.gcs.microservicecommon.data.dto.microservice.login.response.GCSMicroserviceLoginResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.GCSRemoteRequests;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GCSMicroserviceSentryLoginService {

    @Autowired
    GCSRemoteRequests gcsRemoteRequests;

    @Autowired
    GCSMicroserviceGeneralLocalProperties microserviceGeneralLocalProperties;

    @Autowired
    GCSMicroserviceAuthenticationCredentialsInfo gcsMicroserviceAuthenticationCredentialsInfo;

    public void login() throws Exception {
        var password = microserviceGeneralLocalProperties.getPassword();
        if (password == null || password.isEmpty()){
            throw new Exception("Failed to login to GCS Sentry: Password in configuration is null");

        }
        var gcsResponse = gcsRemoteRequests.initiateRequest(GCSMicroserviceLoginResponseDTO.class)
                .destination(GCSMicroservice.GCS_SENTRY, "gcsMicroservice", "login")
                .httpMethod(HttpMethod.POST)
                .setRequestPayload(GCSMicroserviceLoginRequestDTO.fromRawPassword(microserviceGeneralLocalProperties.getMicroserviceName(), password))
                .performRequestWithGCSResponse()
                .toGCSResponse();
        if (gcsResponse.hasError()){
            var error =  gcsResponse.getGcsError();
            throw new Exception("Failed to login to GCS Sentry: " + error.getErrorType().name() + " " + error.getErrorMsg());
        }
        var authResponseDTO = gcsResponse.getResponseData();
        if (authResponseDTO.hasError()){
            throw new Exception("Failed to login to GCS Sentry: " + authResponseDTO.getGcsMicroserviceLoginResponseError().name());
        }
        var token = authResponseDTO.getMicroserviceAuthToken();
        log.info("GCS Microservice Security: Auth token is " + token);
        gcsMicroserviceAuthenticationCredentialsInfo.setMicroserviceAuthToken(token);
    }
}
