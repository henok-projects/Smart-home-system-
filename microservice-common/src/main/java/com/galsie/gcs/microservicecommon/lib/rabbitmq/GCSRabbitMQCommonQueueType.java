package com.galsie.gcs.microservicecommon.lib.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.exchange.GCSRabbitExchange;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GCSRabbitMQCommonQueueType {

    /*
    TEST
     */
    TEST_BINDING(GCSRabbitExchange.TEST, "test.key", "test.queue"),

    /*
    Auth
     */
    USER_AUTH_INFO(GCSRabbitExchange.AUTHENTICATION_DATA, "authdata.users.info.key", "authdata.users.info"), // user auth
    USER_AUTH_LAST_ACCESS(GCSRabbitExchange.AUTHENTICATION_DATA, "authdata.users.lastaccess.key", "authdata.users.lastaccess"), // user auth

    DEVICE_AUTH_INFO(GCSRabbitExchange.AUTHENTICATION_DATA, "authdata.devices.key", "authdata.devices.info"), // device auth
    DEVICE_AUTH_LAST_ACCESS(GCSRabbitExchange.AUTHENTICATION_DATA, "authdata.devices.lastaccess.key", "authdata.devices.lastaccess"), // device auth last access

    GCS_MICROSERVICE_AUTH_INFO(GCSRabbitExchange.AUTHENTICATION_DATA, "authdata.gcsmicroservice.key", "authdata.gcsmicroservice.info"), // microservice auth
    GCS_MICROSERVICE_AUTH_LAST_ACCESS(GCSRabbitExchange.AUTHENTICATION_DATA, "authdata.gcsmicroservice.lastaccess.key", "authinfo.gcsmicroservice.lastaccess"), // microservice auth last access

    GCS_API_CLIENT_AUTH_INFO(GCSRabbitExchange.AUTHENTICATION_DATA, "authdata.gcsapiclient.key", "authdata.gcsapiclient.info"), // api client auth
    GCS_API_CLIENT_AUTH_LAST_ACCESS(GCSRabbitExchange.AUTHENTICATION_DATA, "authdata.gcsapiclient.lastaccess.key", "authdata.gcsapiclient.lastaccess"), // api client auth last access

    /*
    Emails
     */
    EMAILS(GCSRabbitExchange.NOTIFICATION, "emails.key", "emails"),
    SMS(GCSRabbitExchange.NOTIFICATION, "sms.key", "sms"),
    /*
       Resources
     */
    RESOURCE_NOTIFICATION(GCSRabbitExchange.RESOURCE, "resource.gcsmicroservice.key", "resources"), //TODO no service actually uses this queue during final refactoring remove or rename it
    RESOURCE_PROVIDABLE_ASSETS(GCSRabbitExchange.RESOURCE, "resource.gcsmicroservice.providableassets.key", "resources.providableAssets"),
    RESOURCE_GIT_SYNC(GCSRabbitExchange.RESOURCE, "resource.gcsmicroservice.gitsync.key", "resources.gitsync"),
    /*
    Awareness
     */
    GCS_MICROSERVICE_AWARENESS_NOTIFICATIONS(GCSRabbitExchange.GCS_MICROSERVICE_AWARENESS, "awareness.gcsmicroservice.key", "awareness.gcsmicroservice");

    private final GCSRabbitExchange gcsRabbitExchange;
    private final String routingKey;
    public final String defaultQueueName;

    public String getExchangeName() {
        return this.gcsRabbitExchange.getName();
    }
}
