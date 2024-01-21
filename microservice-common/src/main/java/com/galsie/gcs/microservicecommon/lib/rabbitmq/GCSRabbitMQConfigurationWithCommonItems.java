package com.galsie.gcs.microservicecommon.lib.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.galassets.GCSGalAssetsConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.GalSecurityCacheEnabledSessions;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQQueueConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GCSRabbitMQConfigurationWithCommonItems extends GCSRabbitMQConfiguration{

    @Autowired(required = false)
    GalSecurityCacheEnabledSessions galSecurityCacheEnabledSessions; // NIL if no GalSecurityConfiguration

    @Autowired(required = false)
    GCSGalAssetsConfiguration gcsGalAssetsConfiguration;

    public List<GCSRabbitMQQueueConfiguration> getGalSecurityCacheEnabledSessionQueues() {
        if (galSecurityCacheEnabledSessions == null){
            return new ArrayList<>();
        }
        return galSecurityCacheEnabledSessions.getCacheEnabledSessionTypes().stream().map((sessionType) -> {
            var cacheCommonQueueType = sessionType.getSessionListCacheCommonQueueType();
            return GCSRabbitMQQueueConfiguration.builder()
                    .commonQueueType(cacheCommonQueueType)
                    .uniqueQueuePerMicroservice(true)
                    .queueName(null) // null queue name so it takes the name of the GCSRabbitMQCommonQueueType
                    .isDurable(false) // no need to be durable, adds some overhead, but better considering the data isn't that important
                    .isExclusive(true) // IS exclusive so that only this connection can access the queue
                    .build();
        }).collect(Collectors.toList());
    }

    public List<GCSRabbitMQQueueConfiguration> getCommonlyUsedQueues(){
        List<GCSRabbitMQQueueConfiguration> configurationList = new ArrayList<>();
        configurationList.add(GCSRabbitMQQueueConfiguration.builder()
                .commonQueueType(GCSRabbitMQCommonQueueType.RESOURCE_PROVIDABLE_ASSETS)
                .uniqueQueuePerMicroservice(true)
                .isDurable(true)
                .isExclusive(false)
                .autoDelete(false)
                .build());
        configurationList.add(GCSRabbitMQQueueConfiguration.builder()
                .commonQueueType(GCSRabbitMQCommonQueueType.GCS_MICROSERVICE_AWARENESS_NOTIFICATIONS)
                .uniqueQueuePerMicroservice(true)
                .isDurable(true)
                .isExclusive(false)
                .autoDelete(false)
                .build());
        return configurationList;
    }


    /**
     * A Local authenticator receives to a queue updates to when an authentication token for a certain user/device/... was accessed
     * @param sessionType The session type this local authenticator is representing
     * @return The configured queue
     */
    public GCSRabbitMQQueueConfiguration getLocalAuthenticatorLastAccessQueue(GalSecurityAuthSessionType sessionType){
        // get the common queue type related to last access
        var queueType = sessionType.getLastAccessCommonQueueType();
        return GCSRabbitMQQueueConfiguration.builder()
                .commonQueueType(queueType)
                .uniqueQueuePerMicroservice(false) // all of the instances of the local authenticator listen to the same
                .queueName(null) // null queue name so it takes the name of the GCSRabbitMQCommonQueueType
                .isDurable(true) // durable so that we ensure we don't lose any last access time
                .isExclusive(false) // IS exclusive so that other microservices can access this queue
                .build();
    }
}
