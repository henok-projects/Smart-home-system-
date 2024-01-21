package com.galsie.gcs.testservice.configuration;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQQueueConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestGCSRabbitMQConfig extends GCSRabbitMQConfigurationWithCommonItems {

    @Override
    public List<GCSRabbitMQQueueConfiguration> getExchangeBindingConfigurations() {
        List<GCSRabbitMQQueueConfiguration> configList = new ArrayList<>();
        var testQueueConfig = GCSRabbitMQQueueConfiguration.builder().commonQueueType(GCSRabbitMQCommonQueueType.TEST_BINDING).build();
        configList.add(testQueueConfig);
        configList.addAll(this.getCommonlyUsedQueues());
        return configList;
    }

}
