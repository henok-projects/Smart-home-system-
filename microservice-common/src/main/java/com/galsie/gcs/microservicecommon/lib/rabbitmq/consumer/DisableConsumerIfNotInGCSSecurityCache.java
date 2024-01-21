package com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Anotate a method with {@link DisableConsumerIfNotInGCSSecurityCache} if:
 * - It is annotated with {@link GCSRabbitCommonQueueConsumer} or {@link GCSRabbitQueueConsumer}
 * - You only want the consumer to be enabled if the session type associated with this queue is in {@link com.galsie.gcs.microservicecommon.lib.galsecurity.init.GalSecurityCacheEnabledSessions}
 * it can be annotated with
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DisableConsumerIfNotInGCSSecurityCache {
}
