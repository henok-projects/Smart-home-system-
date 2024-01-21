package com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A RabbitMQ consumer defined for a common queue (check {@link GCSRabbitMQCommonQueueType}
 * - NOTE: If a {@link GCSRabbitMQCommonQueueType} binding is used with a name different than the default queue name, can use {@link GCSRabbitQueueConsumer} or the default RabbitMQ queue consumption methods
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GCSRabbitCommonQueueConsumer {
    GCSRabbitMQCommonQueueType[] types();

}
