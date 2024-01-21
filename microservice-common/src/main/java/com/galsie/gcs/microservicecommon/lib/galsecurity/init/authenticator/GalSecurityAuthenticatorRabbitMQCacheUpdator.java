package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.cache.GalSecurityAuthenticatorCacheManager;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.ActiveSessionListDTO;
import com.galsie.gcs.microservicecommon.lib.gcsjackson.GCSObjectMapper;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.DisableConsumerIfNotInGCSSecurityCache;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.GCSRabbitCommonQueueConsumer;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.io.IOException;

/**
 * Defines a {@link GCSRabbitCommonQueueConsumer} for each authentication session types and consumes {@link ActiveSessionListDTO} updates from the queue
 *
 * NOTE: Bean Created in {@link com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration}
 */
public class GalSecurityAuthenticatorRabbitMQCacheUpdator {

    private final static Logger logger = LogManager.getLogger();

    @Autowired
    GalSecurityAuthenticatorCacheManager galSecurityAuthenticatorCacheManager;

    @Autowired
    private GCSObjectMapper objectMapper;

    @GCSRabbitCommonQueueConsumer(types = {GCSRabbitMQCommonQueueType.USER_AUTH_INFO})
    @DisableConsumerIfNotInGCSSecurityCache
    public void updateUserAuthenticationCache(Message message) {
        this.receiveActiveSessionListUpdate(GalSecurityAuthSessionType.USER, message);
    }

    @GCSRabbitCommonQueueConsumer(types = {GCSRabbitMQCommonQueueType.DEVICE_AUTH_INFO})
    @DisableConsumerIfNotInGCSSecurityCache
    public void updateGalDeviceAuthenticationCache(Message message) {
        this.receiveActiveSessionListUpdate(GalSecurityAuthSessionType.GALDEVICE, message);
    }

    @GCSRabbitCommonQueueConsumer(types = {GCSRabbitMQCommonQueueType.GCS_MICROSERVICE_AUTH_INFO})
    @DisableConsumerIfNotInGCSSecurityCache
    public void updateMicroserviceAuthenticationCache(Message message) {
        this.receiveActiveSessionListUpdate(GalSecurityAuthSessionType.GCS_MICROSERVICE, message);
    }

    @GCSRabbitCommonQueueConsumer(types = {GCSRabbitMQCommonQueueType.GCS_API_CLIENT_AUTH_INFO})
    @DisableConsumerIfNotInGCSSecurityCache
    public void updateAPIClientAuthenticationCache(Message message) {
        this.receiveActiveSessionListUpdate(GalSecurityAuthSessionType.GCS_API_CLIENT, message);
    }


    private <idType> void receiveActiveSessionListUpdate(GalSecurityAuthSessionType sessionType, Message message){
        try {
            var activeSessionList = this.extractActiveSessionListDTOFromMessage(message, Long.class);
            this.galSecurityAuthenticatorCacheManager.getSessionListCacheOrAddDefaultFor(sessionType).ifPresent((sessionListCache) -> sessionListCache.updateActiveSessionList(activeSessionList));
        } catch (IOException exception) {
            logger.error("Couldn't receive the active session list, reason: " + exception.getLocalizedMessage());
        }
    }
    private <idType> ActiveSessionListDTO<idType> extractActiveSessionListDTOFromMessage(Message message, Class<idType> idTypeClass) throws IOException {
        JavaType idType = objectMapper.getTypeFactory().constructType(idTypeClass);
        JavaType customClassType = objectMapper.getTypeFactory().constructParametricType(ActiveSessionListDTO.class, idType);
        return objectMapper.readValueFromMessage(message, customClassType);
    }
}
