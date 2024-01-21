package com.galsie.gcs.microservicecommon.config.gcsmicroservice;

import com.sun.istack.NotNull;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Stores the microservice authentication token so that requests to other microservices are authenticated.
 * - Token is set by {@link com.galsie.gcs.microservicecommon.lib.galsecurity.init.login.GCSMicroserviceSentryLoginService}
 * - {@link com.galsie.gcs.microservicecommon.lib.gcsrequests.GCSRemoteRequests} uses this to associate requests with the token
 */
@Component
@Getter
public class GCSMicroserviceAuthenticationCredentialsInfo {
    @NotNull
    Optional<String> microserviceAuthToken = Optional.empty();

    public void setMicroserviceAuthToken(String authToken){
        this.microserviceAuthToken = Optional.ofNullable(authToken);
    }
}
