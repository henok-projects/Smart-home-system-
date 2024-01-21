package com.galsie.gcs.microservicecommon.lib.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.exchange.GCSRabbitExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Provides a convenient mechanism to send messages over RabbitMQ within the GCS's RabbitMQ wrapping system.
 * This class is tailored to work seamlessly with GCS RabbitMQ wrappers, such as {@link GCSRabbitExchange}
 * and {@link GCSRabbitMQCommonQueueType}. The primary aim is to simplify and streamline the process of message
 * broadcasting over RabbitMQ channels, ensuring that messaging remains consistent and adheres to GCS standards.
 * this is defined as a bean in {@link GCSRabbitMQConfiguration} this means it is only visible if the implementing microservice has a GCSRabbitMQConfiguration configuration child
 */
public class GCSRabbitTemplate {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public GCSRabbitTemplate(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Given a {@link GCSRabbitMQCommonQueueType} we discern the exchange name and routing key, using them to broadcast
     * the provided object over RabbitMQ. This method uses the default values found in the {@link GCSRabbitMQCommonQueueType}
     */
    public void convertAndSend(GCSRabbitMQCommonQueueType commonQueueType, Object o){
        convertAndSend(commonQueueType.getExchangeName(), commonQueueType.getRoutingKey(), o);
    }

    /**
     * Given a {@link GCSRabbitMQCommonQueueType} we discern the exchange name and together with the provided custom routing key, broadcast
     * the provided object over RabbitMQ. This method uses only the default {@link GCSRabbitExchange#getName()} value found in {@link GCSRabbitMQCommonQueueType}
     */
    public void convertAndSend(GCSRabbitMQCommonQueueType commonQueueType, String routingKey, Object o){
        convertAndSend(commonQueueType.getExchangeName(), routingKey, o);
    }

    /**
     * Given a {@link GCSRabbitExchange} we discern the exchange name and together with the provided custom routing key, broadcast
     * the provided object over RabbitMQ
     */
    public void convertAndSend(GCSRabbitExchange exchange, String routingKey, Object o){
        convertAndSend(exchange.getName(), routingKey, o);
    }

    /**
     * Broadcasts the provided object over RabbitMQ using the provided exchange name and routing key
     */
    public void convertAndSend(String exchangeName, String routingKey, Object o){
        this.rabbitTemplate.convertAndSend(exchangeName, routingKey, o);
    }

}
