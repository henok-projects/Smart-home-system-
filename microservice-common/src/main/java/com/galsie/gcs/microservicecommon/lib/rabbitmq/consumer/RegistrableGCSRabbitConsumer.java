package com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer;

import com.galsie.gcs.microservicecommon.lib.rabbitmq.init.GCSRabbitQueueConsumerAutoConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.lang.reflect.Method;

@AllArgsConstructor
@Getter
public class RegistrableGCSRabbitConsumer {
    private Object delegate;
    private String methodName;
    private String[] queueNames;
}