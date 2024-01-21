package com.galsie.gcs.microservicecommon.lib.gcsjackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.IOException;


public class GCSObjectMapper extends ObjectMapper {

    /**
     * Because we use Java time information in DTOs like {@link com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO}
     * we need to register the {@link JavaTimeModule} to the {@link ObjectMapper} so that it can be used to serialize and deserialize the DTOs
     */
    public GCSObjectMapper() {
        this.registerModule(new JavaTimeModule());
    }
    public <T> T readValueFromMessage(Message message, Class<T> tClass) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String contentType = properties.getContentType();
        if (!MessageProperties.CONTENT_TYPE_JSON.equals(contentType)) {
            throw new IllegalArgumentException("Expected JSON message, got: " + contentType);
        }
        return this.readValue(message.getBody(), tClass);
    }

    public <T> T readValueFromMessage(Message message, JavaType typeReference) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String contentType = properties.getContentType();
        if (!MessageProperties.CONTENT_TYPE_JSON.equals(contentType)) {
            throw new IllegalArgumentException("Expected JSON message, got: " + contentType);
        }
        return this.readValue(message.getBody(), typeReference);
    }

    public <T> T readValueFromMessage(Message message, TypeReference<T> typeReference) throws IOException {
        MessageProperties properties = message.getMessageProperties();
        String contentType = properties.getContentType();
        if (!MessageProperties.CONTENT_TYPE_JSON.equals(contentType)) {
            throw new IllegalArgumentException("Expected JSON message, got: " + contentType);
        }
        return this.readValue(message.getBody(), typeReference);
    }
}
