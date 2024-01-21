package com.galsie.gcs.microservicecommon.lib.rabbitmq.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.amqp.core.AbstractExchange;

/**
 *
 * When you attempt to declare an exchange that already exists with a different type,
 * RabbitMQ will return an error, indicating that the pre-existing exchange type does not match the type in the new declaration.
 */
@AllArgsConstructor
@Getter
public enum GCSRabbitExchange {
    TEST("test/test-exchange", ExchangeType.TOPIC),

    NOTIFICATION("notification-exchange", ExchangeType.TOPIC), //This exchange is used for sms and email notifications
    AUTHENTICATION_DATA("auth-data-exchange", ExchangeType.TOPIC),
    DEVICE_DATA("device-data-exchange", ExchangeType.TOPIC),
    RESOURCE("resource-exchange", ExchangeType.TOPIC),
    GCS_MICROSERVICE_AWARENESS("gcs-microservice-awareness-exchange", ExchangeType.TOPIC);

    private final String name;
    private final ExchangeType exchangeType;

    public AbstractExchange getNewExchangeInstance(){
        return exchangeType.newInstance(name);
    }


}
