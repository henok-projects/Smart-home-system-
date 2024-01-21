package com.galsie.gcs.microservicecommon.lib.gcsawareness;

import org.springframework.context.annotation.Bean;

/**
 * This is implemented by  another class in microservice that wants to interface with Microservice Awareness, allowing
 * the microservice consume and produce events related to Microservice Awareness.
 */

public abstract class GCSMicroserviceAwarenessConfiguration {

    @Bean
    public GCSMicroserviceAwarenessRabbitMQPublisher getAwarenessPublisher(){
        return new GCSMicroserviceAwarenessRabbitMQPublisher();
    }

    @Bean
    public GCSMicroserviceAwarenessRabbitMqConsumer getAwarenessConsumer(){
        return new GCSMicroserviceAwarenessRabbitMqConsumer();
    }

}
