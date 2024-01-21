package com.galsie.gcs.microservicecommon.lib.email;

import com.galsie.gcs.microservicecommon.lib.email.data.discrete.EmailType;
import com.galsie.gcs.microservicecommon.lib.email.data.dto.request.SendEmailRequestDTO;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;

@Slf4j
public class GCSRemoteEmailSender {

    @Autowired(required = false)
    @Lazy // lazy because of cyclic dependency due to rabbitmq conf with common items depending on security
    GCSRabbitTemplate gcsRabbitTemplate;

    public void sendEmail(EmailType emailType, String toAddress, HashMap<String, String> variables){
        if (gcsRabbitTemplate == null){
            log.info("Can't send email, GCSRabbitTemplate is null");
            return;
        }
        gcsRabbitTemplate.convertAndSend(GCSRabbitMQCommonQueueType.EMAILS, new SendEmailRequestDTO(emailType, toAddress, variables));
    }
}
