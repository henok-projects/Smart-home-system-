package com.galsie.gcs.microservicecommon.lib.rabbitmq;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceGeneralLocalProperties;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.RegistrableGCSRabbitConsumerList;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.init.GCSRabbitQueueConsumerAutoConfig;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQConfiguredQueue;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQConfiguredQueuesProvider;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQQueueConfiguration;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.init.GCSRabbitMQConfiguredQueuesInitializer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>This is an abstract configuration class that aids in the setup of common exchange to queue bindings in RabbitMQ. It provides
 * a convenient way to define these bindings across multiple microservices, with 2 options:</p>
 *
 * <p><b>1. Each microservice receives its own copy of the messages</b> (distinct queue set to true in {@link GCSRabbitMQQueueConfiguration})</p>
 * <p>The main idea is to bind different queues (use the random uid of the microservice) to the same exchange and routing key. This way, each microservice
 * can process the same messages independently. This approach is useful when you want to distribute the same
 * messages to multiple consumers, or microservices, each having their own queue.</p>
 *
 * <p><b>2. Microservices share the same queue</b> (distinct queue set to false in {@link GCSRabbitMQQueueConfiguration})</p>
 * <p>In this case, microservices share the queue name for the binding thus sharing the messages</p>
 * <hr>
 * <p>Subclasses of this abstract class should implement the {@link GCSRabbitMQConfiguration#getExchangeBindingConfigurations()} method to return a list of
 * {@link GCSRabbitMQQueueConfiguration} which is used should be used to create the exchanges, queues, and bindings.</p>
 *
 * <p>A {@link RabbitAdmin} instance is used to declare the exchanges, queues, and bindings based on the list of {@link GCSRabbitMQConfiguration#getExchangeBindingConfigurations()}.
 * This is done in the `createExchangesAndBindings` method, which is annotated with `@PostConstruct` to ensure that it runs once the application context is fully initialized.</p>
 *
 * <p>This way, the RabbitMQ setup for each microservice can be centralized in one place, making it easier to manage and modify as needed.</p>
 *
 * <p><b>NOTE:</b> You can autowire {@link GCSRabbitMQConfiguredQueuesProvider} to get the registered bindings.</p>
 */

public abstract class GCSRabbitMQConfiguration {

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Jackson2JsonMessageConverter jackson2JsonMessageConverter;

    @Autowired
    GCSMicroserviceGeneralLocalProperties gcsMicroserviceGeneralLocalProperties;
    /**
     Get a list of queues this microservice defines
     - Needn't give queue names, but if a queue with that name already exists, an error would be thrown
     */
    public abstract List<GCSRabbitMQQueueConfiguration> getExchangeBindingConfigurations();

    @Bean
    public RabbitAdmin rabbitAdmin(){
        return new RabbitAdmin(connectionFactory);
    }


    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    @Primary
    GCSRabbitTemplate gcsRabbitTemplate(){
        return new GCSRabbitTemplate(rabbitTemplate());
    }


    /**
     Sets {@link GCSRabbitMQConfiguredQueuesProvider} as a bean so that we can conveniently know which bindings are enabled
     - Used by {@link GCSRabbitMQConfiguredQueuesInitializer} to create the exchanges, queues, bindings
     */
    @Bean
    public GCSRabbitMQConfiguredQueuesProvider gcsRabbitMQConfiguredQueuesProvider() {
        var registeredQueues = getExchangeBindingConfigurations()
                .stream()
                .map((config) -> GCSRabbitMQConfiguredQueue.fromConfiguration(gcsMicroserviceGeneralLocalProperties, config))
                .collect(Collectors.toList());
        return new GCSRabbitMQConfiguredQueuesProvider(registeredQueues);
    }

    /**
     * Sets as a bean so we can conveniently get the list of registrable rabbitmq consumers (loaded through autoconfig)
     * - Used by {@link GCSRabbitMQConfiguredQueuesInitializer} to register the rabbitmq consumers
     */
    @Bean
    public RegistrableGCSRabbitConsumerList gcsRabbitConsumerList(){
        return new RegistrableGCSRabbitConsumerList();
    }

    /**
     * To find all registrable consumers
     * @return
     */
    @Bean
    public GCSRabbitQueueConsumerAutoConfig gcsRabbitQueueConsumerAutoConfigRegistrableConsumersProvider(){
        return new GCSRabbitQueueConsumerAutoConfig();
    }

    /**
     */
    @Bean
    public GCSRabbitMQConfiguredQueuesInitializer gcsRabbitMQInitializer() {
        return new GCSRabbitMQConfiguredQueuesInitializer();
    }


}

