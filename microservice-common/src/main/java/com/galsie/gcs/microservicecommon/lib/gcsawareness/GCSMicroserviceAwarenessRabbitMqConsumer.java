package com.galsie.gcs.microservicecommon.lib.gcsawareness;

import com.galsie.gcs.microservicecommon.lib.gcsjackson.GCSObjectMapper;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.GCSRabbitCommonQueueConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
public class GCSMicroserviceAwarenessRabbitMqConsumer {

    @Autowired
    GCSMicroserviceAwarenessService gcsMicroserviceAwarenessService;

    GCSObjectMapper mapper = new GCSObjectMapper();

    
    @GCSRabbitCommonQueueConsumer(types = GCSRabbitMQCommonQueueType.GCS_MICROSERVICE_AWARENESS_NOTIFICATIONS)
    public void consumeAwarenessStatus(Message message) {
        try {
            var awarenessBroadcastDTO = mapper.readValueFromMessage(message, GCSMicroserviceAwarenessStatusDTO.class);
            gcsMicroserviceAwarenessService.updateSomeMicroserviceAwarenessStatus(awarenessBroadcastDTO);
        }catch (IOException e){
            log.error("Error while consuming awareness status", e);
        }
    }

}
