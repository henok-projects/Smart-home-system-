package com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GCSRabbitQueueConsumer {
    String[] queues();
}
