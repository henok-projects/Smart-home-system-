package com.galsie.gcs.microservicecommon.lib.rabbitmq.queues;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Note: Bean created in {@link com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQConfiguration}
 */
@AllArgsConstructor
@Getter
public class GCSRabbitMQConfiguredQueuesProvider {
    // maps a binding to a queue name
    private List<GCSRabbitMQConfiguredQueue> gcsRabbitMQConfiguredQueuesList;

}
