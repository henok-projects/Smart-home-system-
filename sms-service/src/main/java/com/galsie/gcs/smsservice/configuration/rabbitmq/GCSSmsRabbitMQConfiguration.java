package com.galsie.gcs.smsservice.configuration.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQConfiguration;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQQueueConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GCSSmsRabbitMQConfiguration extends GCSRabbitMQConfiguration {


    @Override
    public List<GCSRabbitMQQueueConfiguration> getExchangeBindingConfigurations() {
        var smsQueueConfig = GCSRabbitMQQueueConfiguration.builder()
                .commonQueueType(GCSRabbitMQCommonQueueType.SMS)
                .isDurable(true)
                .isExclusive(false)
                .autoDelete(false)
                .build();

        return Arrays.asList(smsQueueConfig);
    }
}
