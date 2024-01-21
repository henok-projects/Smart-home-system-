package com.galsie.gcs.microservicecommon.lib.galsecurity.session;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import lombok.Getter;

@Getter
public enum GalSecurityAuthSessionType {
    USER(GCSRabbitMQCommonQueueType.USER_AUTH_INFO, GCSRabbitMQCommonQueueType.USER_AUTH_LAST_ACCESS, "userAuthToken"),
    GALDEVICE(GCSRabbitMQCommonQueueType.DEVICE_AUTH_INFO, GCSRabbitMQCommonQueueType.DEVICE_AUTH_LAST_ACCESS, "galDeviceAuthToken"),
    GCS_MICROSERVICE(GCSRabbitMQCommonQueueType.GCS_MICROSERVICE_AUTH_INFO, GCSRabbitMQCommonQueueType.GCS_MICROSERVICE_AUTH_LAST_ACCESS,"microserviceAuthToken"),
    GCS_API_CLIENT(GCSRabbitMQCommonQueueType.GCS_API_CLIENT_AUTH_INFO,  GCSRabbitMQCommonQueueType.GCS_API_CLIENT_AUTH_LAST_ACCESS, "gcsApiKey", "apiClientDeviceName"),

    TEST(GCSRabbitMQCommonQueueType.GCS_API_CLIENT_AUTH_INFO, GCSRabbitMQCommonQueueType.GCS_MICROSERVICE_AUTH_LAST_ACCESS, "testAuthToken");

    private String[] tokenHeaderParams;
    private GCSRabbitMQCommonQueueType sessionListCacheCommonQueueType;
    private GCSRabbitMQCommonQueueType lastAccessCommonQueueType; // local authenticator microservice uses this to know when the session was last accessed. Other services publish to this
    private GalSecurityAuthSessionType(GCSRabbitMQCommonQueueType sessionListCacheCommonQueueType, GCSRabbitMQCommonQueueType lastAccessCommonQueueType, String... tokenHeaderParams){
        this.sessionListCacheCommonQueueType = sessionListCacheCommonQueueType;
        this.lastAccessCommonQueueType = lastAccessCommonQueueType;
        this.tokenHeaderParams = tokenHeaderParams;
    }

    public static GalSecurityAuthSessionType[] implementedAuthenticators(){
        return new GalSecurityAuthSessionType[]{USER, GALDEVICE, GCS_MICROSERVICE, GCS_API_CLIENT};
    }
}
