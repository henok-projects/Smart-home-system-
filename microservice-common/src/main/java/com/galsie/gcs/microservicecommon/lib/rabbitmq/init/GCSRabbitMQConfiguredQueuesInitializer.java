package com.galsie.gcs.microservicecommon.lib.rabbitmq.init;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.RegistrableGCSRabbitConsumerList;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQConfiguredQueue;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQConfiguredQueuesProvider;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class GCSRabbitMQConfiguredQueuesInitializer {

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired
    GCSRabbitMQConfiguredQueuesProvider rabbitMQRegisteredQueuesProvider;

    @Autowired
    RegistrableGCSRabbitConsumerList registrableGCSRabbitConsumerList;
    /*
   After all beans are initiated
    */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (rabbitMQRegisteredQueuesProvider == null) {
            return;
        }
        createExchangesAndBindings();
        registrableGCSRabbitConsumerList.registerAll();
    }
    /*
    Exchanges and bindings
     */
    private void createExchangesAndBindings() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        for (GCSRabbitMQConfiguredQueue registeredCommonQueue : rabbitMQRegisteredQueuesProvider.getGcsRabbitMQConfiguredQueuesList()) {
            createBinding(rabbitAdmin, registeredCommonQueue);
        }
    }

    private void createBinding(RabbitAdmin rabbitAdmin, GCSRabbitMQConfiguredQueue registeredCommonQueue) {
        var commonQueueConfig = registeredCommonQueue.getCommonQueueConfig();
        var commonQueueType = commonQueueConfig.getCommonQueueType();
        // Define the exchange
        var exchange = commonQueueType.getGcsRabbitExchange().getNewExchangeInstance();
        rabbitAdmin.declareExchange(exchange);
        // Define the queue (NOTE that if a queue with that name already exists, an error would be thrown) (Also note if its exclusive that won't be the case unless its 2 erxc lusive queues with the same name)
        Queue queue = new Queue(registeredCommonQueue.getQueueName(), commonQueueConfig.isDurable(), commonQueueConfig.isExclusive(), commonQueueConfig.isAutoDelete(), commonQueueConfig.getArguments());
        rabbitAdmin.declareQueue(queue);
        var key = commonQueueConfig.getUsableRoutingKey();
        // Define the binding
        var bindingDefinition = BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(key).noargs();
        rabbitAdmin.declareBinding(bindingDefinition);
    }



}
