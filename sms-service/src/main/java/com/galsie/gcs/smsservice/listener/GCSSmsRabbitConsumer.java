package com.galsie.gcs.smsservice.listener;

import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSRequestDTO;
import com.galsie.gcs.microservicecommon.lib.gcsjackson.GCSObjectMapper;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.GCSRabbitCommonQueueConsumer;
import com.galsie.gcs.smsservice.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class GCSSmsRabbitConsumer {

    @Autowired
    GCSObjectMapper objectMapper;

    @Autowired
    SmsService smsService;

    @GCSRabbitCommonQueueConsumer(types = GCSRabbitMQCommonQueueType.SMS)
    public void consumeSMSRequest(Message message){
        var smsRequestDTOOpt = fromMessage(message, SMSRequestDTO.class);
        if (smsRequestDTOOpt.isEmpty()){
            log.info("Invalid SMS message received..");
            return;
        }
        var smsRequestDTO = smsRequestDTOOpt.get();
        this.sendSMS(smsRequestDTO);

    }

    private void sendSMS(SMSRequestDTO smsRequestDTO){
        var response = smsService.sendSMS(smsRequestDTO);
        if (response.hasError()){
            log.info("Failed to send SMS, reason: " + response.getGcsError().getErrorType().name());
            return;
        }
        var responseDTO = response.getResponseData();
        if (responseDTO.hasError()){
            log.info("Failed to send SMS, reason: " + responseDTO.getError());
            return;
        }
        log.info("SMS to " + smsRequestDTO.getPhoneNumber() + " was sent!");
    }

    private <T> Optional<T> fromMessage(Message message, Class<T> tClass){
        try {
            return Optional.of(objectMapper.readValue(message.getBody(), tClass));
        } catch (IOException e) {
            log.warn("Failed to parse message, reason: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

}
