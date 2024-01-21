package com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer;

import lombok.Getter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean created in {@link com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQConfiguration}
 */
@Getter
public class RegistrableGCSRabbitConsumerList {

    @Autowired
    RabbitAdmin rabbitAdmin;

    private final List<RegistrableGCSRabbitConsumer> registrableGCSRabbitConsumerList = new ArrayList<>();

    public void add(RegistrableGCSRabbitConsumer registrableGCSRabbitConsumer){
        registrableGCSRabbitConsumerList.add(registrableGCSRabbitConsumer);
    }

    public void registerAll(){
        for (RegistrableGCSRabbitConsumer consumer: this.registrableGCSRabbitConsumerList) {
            this.createRabbitListener(consumer);
        }
    }

    public void createRabbitListener(RegistrableGCSRabbitConsumer registrableGCSRabbitListener){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(rabbitAdmin.getRabbitTemplate().getConnectionFactory());
        //MessageListenerAdapter adapter = new MessageListenerAdapter(registrableGCSRabbitListener.getDelegate(), registrableGCSRabbitListener.getMethodName());

        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    Method method = registrableGCSRabbitListener.getDelegate().getClass().getMethod(registrableGCSRabbitListener.getMethodName(), Message.class);
                    method.invoke(registrableGCSRabbitListener.getDelegate(), message);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle exception properly
                }
            }
        };

        container.setMessageListener(messageListener);
        container.addQueueNames(registrableGCSRabbitListener.getQueueNames());
        container.start();
    }
}
