package com.galsie.gcs.microservicecommon.lib.rabbitmq.exchange;

import org.springframework.amqp.core.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum ExchangeType {

    DIRECT(DirectExchange.class),
    TOPIC( TopicExchange.class),
    FANOUT( FanoutExchange.class),
    HEADERS( HeadersExchange.class);

    private final Class<? extends AbstractExchange> exchangeClass;

    ExchangeType(Class<? extends AbstractExchange> exchangeClass) {
        this.exchangeClass = exchangeClass;
    }

    public AbstractExchange newInstance(String exchangeName) {
        try {
            Constructor<? extends AbstractExchange> constructor = exchangeClass.getConstructor(String.class);
            return constructor.newInstance(exchangeName);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Unable to create new instance of exchange type", e);
        }
    }
}
