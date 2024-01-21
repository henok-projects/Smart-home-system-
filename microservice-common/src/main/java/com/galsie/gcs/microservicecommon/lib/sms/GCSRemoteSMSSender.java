package com.galsie.gcs.microservicecommon.lib.sms;

import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSType;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSRequestDTO;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Map;

@Slf4j
public class GCSRemoteSMSSender {

    @Autowired(required = false)
    @Lazy
    GCSRabbitTemplate gcsRabbitTemplate;

    public void sendSms(SMSType smsType, short countryCode, String destinationPhoneNumber, Map<String, String> variableReplacement){
        if (gcsRabbitTemplate == null){
            log.error("Failed to Send SMS message, reason: GCSRabbitTemplate is null.");
            return;
        }
        gcsRabbitTemplate.convertAndSend(GCSRabbitMQCommonQueueType.SMS, new SMSRequestDTO(smsType, countryCode, destinationPhoneNumber, variableReplacement));
    }

}
