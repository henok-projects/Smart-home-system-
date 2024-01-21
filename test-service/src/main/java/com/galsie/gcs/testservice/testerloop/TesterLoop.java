package com.galsie.gcs.testservice.testerloop;

import com.galsie.gcs.microservicecommon.lib.email.GCSRemoteEmailSender;
import com.galsie.gcs.microservicecommon.lib.email.data.discrete.EmailType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManager;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.testservice.events.TestGCSEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.HashMap;

@AllArgsConstructor
@Slf4j
public class TesterLoop implements Runnable {
    GCSEventManager gcsGlobalEventManager;
    RabbitTemplate rabbitTemplate;
    GCSRemoteEmailSender gcsRemoteEmailSender;

    public void loop() {
        testEventCalling();
        sleep(1000);
        sendTestRabbitMQMessage("HELLO");
        var map = new HashMap<String, String>();
        map.put("verification_code", "1234");
        gcsRemoteEmailSender.sendEmail(EmailType.USER_EMAIL_VERIFICATION, "liwaa@galsie.com", map);
        log.info("Sent email");
        sleep(1000);
    }

    private void testEventCalling(){
        var name = "LiwaaK " + String.valueOf((int) (Math.random() * 10));
        log.info("Calling Event with name " + name);
        gcsGlobalEventManager.callEvent(new TestGCSEvent(name));
    }

    private void sendTestRabbitMQMessage(String msg){
        log.info("Sending rabbitmq message " + msg);
        String exchangeName = GCSRabbitMQCommonQueueType.TEST_BINDING.getExchangeName();
        String routingKey = GCSRabbitMQCommonQueueType.TEST_BINDING.getRoutingKey();
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg);
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
            log.info("THREAD FAILED TO SLEEP");
        }
    }

    @Override
    public void run() {
        while (true) {
            loop();
        }
    }

}
