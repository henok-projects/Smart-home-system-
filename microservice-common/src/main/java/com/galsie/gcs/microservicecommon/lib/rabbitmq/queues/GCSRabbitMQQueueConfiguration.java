package com.galsie.gcs.microservicecommon.lib.rabbitmq.queues;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * Note: Default arguments here match those in rabbitmq
 * - Except for the queue name, which would default to the GCSRabbitMQCommonQueueType in {@link GCSRabbitMQConfiguredQueue#fromConfiguration(GCSRabbitMQQueueConfiguration)}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GCSRabbitMQQueueConfiguration {
        private GCSRabbitMQCommonQueueType commonQueueType;
        /*
        - The 'distinctQueue' flag determines how the queue name is registered for the binding:
        - If set to true: 
          - A unique queue name is created for the binding. This is typically done when multiple microservices need to read the same data from an exchange for this binding. 
          - Here, a unique queue is assigned to each microservice. This implies that each microservice is bound to the exchange using a different queue than the others. 
          - Consequently, the exchange routes the message to all these distinct queues.
        - If set to false: 
          - The queue name will match the enum item name. This setting is used when the objective is to prevent multiple microservices from consuming the same message from the queue. Once a message is consumed by one microservice, it becomes unavailable to others.
        */
        @Nullable
        private String queueName = null; // if null, the name of the commonExchangeBinding is used
        private boolean uniqueQueuePerMicroservice = false; // If true, then the queue name is prefixed with the unique id of this microservice instance so that its for this queue
        private boolean isDurable = true; // Durable queues remain active and keep their data intact even if RabbitMQ server restarts
        private boolean isExclusive = false; // Exclusive queues can only be accessed by the connection that declared them and are deleted when that connection closes. Useful for a distinct queue per microservice
        private boolean autoDelete = false;  // Auto-delete queues get deleted once the last consumer unsubscribes.
        private String customRoutingKey = null; //If a custom routing key is provided, it is used instead of the one defined in the GCSRabbitMQCommonQueueType

        @Nullable
        private Map<String, Object> arguments = null;

        public String getUsableRoutingKey(){
                return customRoutingKey != null ? customRoutingKey : commonQueueType.getRoutingKey();
        }
}