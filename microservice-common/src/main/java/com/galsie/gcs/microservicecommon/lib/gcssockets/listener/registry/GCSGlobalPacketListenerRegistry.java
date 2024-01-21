package com.galsie.gcs.microservicecommon.lib.gcssockets.listener.registry;

import com.galsie.gcs.microservicecommon.lib.gcssockets.handler.GCSSocketHandler;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.Packet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Method;
import java.util.HashMap;

@Slf4j
public class GCSGlobalPacketListenerRegistry {
    public final HashMap<Class<? extends GCSSocketHandler>, GCSSocketHandlerPacketListenerRegistry> registryHashMap = new HashMap<>();

    private GCSSocketHandlerPacketListenerRegistry getHandlerPacketListenerRegistry(Class<? extends GCSSocketHandler> handlerClass){
         if (registryHashMap.containsKey(handlerClass)){
             return registryHashMap.get(handlerClass);
         }
         var handlerPacketListenerRegistry = new GCSSocketHandlerPacketListenerRegistry(handlerClass);
         registryHashMap.put(handlerClass, handlerPacketListenerRegistry);
         return handlerPacketListenerRegistry;
    }

    public void addEntry(Class<? extends GCSSocketHandler> handlerClass, Class<? extends Packet> packet, Object bean, Method method) {
        var handlerPacketListenerRegistry = getHandlerPacketListenerRegistry(handlerClass);
        handlerPacketListenerRegistry.registerOnPacketMethod(packet, bean, method);
    }

    public void receivePacket(Class<? extends GCSSocketHandler> handlerClass, WebSocketSession session, Packet packet) {
        var registryEntry = getHandlerPacketListenerRegistry(handlerClass);
        registryEntry.receivePacket(session, packet);
    }
}
