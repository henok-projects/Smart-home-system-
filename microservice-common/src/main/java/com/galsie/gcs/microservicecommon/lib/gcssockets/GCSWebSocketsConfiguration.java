package com.galsie.gcs.microservicecommon.lib.gcssockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.gcssockets.handler.GCSSocketHandlerConfiguration;
import com.galsie.gcs.microservicecommon.lib.gcssockets.init.GCSGlobalPacketListenerRegistryAutoConf;
import com.galsie.gcs.microservicecommon.lib.gcssockets.listener.registry.GCSGlobalPacketListenerRegistry;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.AbstractPacketType;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.serializer.GCSPacketSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GCSWebSocketsConfiguration implements WebSocketConfigurer {

    @Autowired
    ObjectMapper objectMapper;

    public abstract <T extends AbstractPacketType> T[] getPacketTypes();

    public abstract List<GCSSocketHandlerConfiguration> getSocketHandlersConfiguration();

    @Bean
    GCSPacketSerializer gcsPacketSerializer(){
        var map = new HashMap<Long, AbstractPacketType>();
        for (var packetType: getPacketTypes()){
            map.put(packetType.getId(), packetType);
        }
        return new GCSPacketSerializer(objectMapper, map);
    }
    @Bean
    public GCSGlobalPacketListenerRegistry gcsGlobalPacketListenerRegistry(){
        return new GCSGlobalPacketListenerRegistry();
    }

    @Bean
    GCSGlobalPacketListenerRegistryAutoConf gcsGlobalPacketListenerRegistryAutoConf(){
        return new GCSGlobalPacketListenerRegistryAutoConf();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry){
        for (var handlerConfig: getSocketHandlersConfiguration()){
            var handler = handlerConfig.getGcsSocketHandler();
            handler.gcsPacketSerializer = gcsPacketSerializer();
            handler.gcsGlobalPacketListenerRegistry = gcsGlobalPacketListenerRegistry();
            webSocketHandlerRegistry.addHandler(handler, handlerConfig.getPath());
        }
    }

}
