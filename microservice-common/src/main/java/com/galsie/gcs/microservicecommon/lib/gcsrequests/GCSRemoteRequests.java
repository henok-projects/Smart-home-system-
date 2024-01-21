package com.galsie.gcs.microservicecommon.lib.gcsrequests;

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceAuthenticationCredentialsInfo;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.login.GCSMicroserviceSentryLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Bean initialized in {@link GCSRemoteRequestsConfiguration}
 */
public class GCSRemoteRequests {

    @Autowired
    WebClient.Builder webClientBuilder;

    @Autowired
    GCSMicroserviceAuthenticationCredentialsInfo gcsMicroserviceAuthenticationCredentialsInfo;

    @Autowired(required = false)
    @Lazy
    GalSecurityConfiguration galSecurityConfiguration;

    @Autowired(required = false)
    @Lazy
    GCSMicroserviceSentryLoginService gcsMicroserviceSentryLoginService;
    /**
     * - Adds the gcsMicroservice authentication token
     * - Adds the {@link GCSMicroserviceSentryLoginService} if auto login with sentry is enabled in the {@link GalSecurityConfiguration} of the implementing microservice
     * @param responseBodyType
     * @param <T>
     * @return
     */
    public <T> GCSRemoteRequest<T> initiateRequest(Class<T> responseBodyType){
        var builder =  new GCSRemoteRequest<T>()
                .setWebClientBuilder(this.webClientBuilder)
                .setResponseBodyType(responseBodyType);
        // Add the login service if auto login is enabled
        if (galSecurityConfiguration != null && gcsMicroserviceSentryLoginService != null && galSecurityConfiguration.isMicroserviceAutoLoginWithSentryEnabled()){
            builder.setGCSMicroserviceSentryLoginService(this.gcsMicroserviceSentryLoginService);
        }
        // add the auth token if present
        builder.setGCSMicroserviceAuthCredentialsInfo(gcsMicroserviceAuthenticationCredentialsInfo);
        return builder;
    }
}
