package com.galsie.gcs.homes.configuration.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQQueueConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class HomesRabbitMQConfiguration extends GCSRabbitMQConfigurationWithCommonItems {


    @Override
    public List<GCSRabbitMQQueueConfiguration> getExchangeBindingConfigurations() {
        List<GCSRabbitMQQueueConfiguration> configList = new ArrayList<>();
        /**
        Add the configuration for the session caching queues.
        NOTE: These {@link GalSecurityAuthSessionType} for which caching is enabled are defined in {@link com.galsie.gcs.homes.configuration.security.HomesSecurityConfiguration}
         */
        configList.addAll(this.getGalSecurityCacheEnabledSessionQueues());
        configList.addAll(this.getCommonlyUsedQueues());
        return configList;
    }
}
