package com.galsie.gcs.testservice.listeners.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.GCSRabbitCommonQueueConsumer;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class TestGCSRabbitListener {

    @GCSRabbitCommonQueueConsumer(types = GCSRabbitMQCommonQueueType.TEST_BINDING)
    public void onMessage(Message message){
        log.info("RECEIVED str " + new String(message.getBody(), StandardCharsets.UTF_8));
    }

    /*
    @GCSRabbitCommonQueueConsumer(types = GCSRabbitMQCommonQueueType.TEST_BINDING)
    public void onMessage2(Message message){
        log.info("RECEIVED str 2" + new String(message.getBody(), StandardCharsets.UTF_8));
    }
    */

}
