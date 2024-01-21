package com.galsie.gcs.resources.config.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQQueueConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class ResourcesRabbitMQConfiguration extends GCSRabbitMQConfigurationWithCommonItems {


    @Override
    public List<GCSRabbitMQQueueConfiguration> getExchangeBindingConfigurations() {
        List<GCSRabbitMQQueueConfiguration> configurationList = new ArrayList<>(getCommonlyUsedQueues());
        var resourceGitSyncConfig = GCSRabbitMQQueueConfiguration.builder()
                .commonQueueType(GCSRabbitMQCommonQueueType.RESOURCE_GIT_SYNC)
                .isDurable(true)
                .isExclusive(false)
                .autoDelete(false)
                .build();
        log.warn("RabbitMQ configuration for resources-service completed");
        configurationList.add(resourceGitSyncConfig);
        configurationList.addAll(getGalSecurityCacheEnabledSessionQueues());
        return configurationList;
    }
}
