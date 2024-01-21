package com.galsie.gcs.users.configuration.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQQueueConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class UsersRabbitMqConfiguration extends GCSRabbitMQConfigurationWithCommonItems {

    @Override
    public List<GCSRabbitMQQueueConfiguration> getExchangeBindingConfigurations() {
        var queueList = new ArrayList<GCSRabbitMQQueueConfiguration>();

        // ADD queues for cache enabled sessions, cache enabled sessions are defined in UserGalSecurityConfiguration
        // NOTE: we only cache for remote authentication (like microservice, api client, or device)
        var cacheEnabledSessionQueues = this.getGalSecurityCacheEnabledSessionQueues(); // Add these to that the GalSecurityCacheUpdator (defined in GalSecurityConfiguration) can be used
        queueList.addAll(cacheEnabledSessionQueues);

        // Get configuration for a last access queue, so that we can get the last access times of user sessions form remote services
        queueList.add(getLocalAuthenticatorLastAccessQueue(GalSecurityAuthSessionType.USER));
        return queueList;
    }
}
