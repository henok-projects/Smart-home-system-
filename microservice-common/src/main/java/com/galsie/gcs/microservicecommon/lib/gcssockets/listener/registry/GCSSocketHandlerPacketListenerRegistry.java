package com.galsie.gcs.microservicecommon.lib.gcssockets.listener.registry;

import com.galsie.gcs.microservicecommon.lib.gcssockets.handler.GCSSocketHandler;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.Packet;
import com.galsie.gcs.microservicecommon.lib.gcssockets.GalWebSocketSession;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Method;
import java.util.*;


public class GCSSocketHandlerPacketListenerRegistry {

    private Map<Class<? extends Packet>, List<RegisteredOnPacketMethod>> registeredOnPacketMethods = new HashMap<>();

    private Class<? extends GCSSocketHandler> handlerClass;
    public GCSSocketHandlerPacketListenerRegistry(Class<? extends GCSSocketHandler> handlerClass){
        this.handlerClass = handlerClass;
    }

    private List<RegisteredOnPacketMethod> getRegisteredOnPacketMethodListFor(Class<? extends Packet> packetClass){
        if (registeredOnPacketMethods.containsKey(packetClass)){
            return registeredOnPacketMethods.get(packetClass);
        }
        var newList = new ArrayList<RegisteredOnPacketMethod>();
        registeredOnPacketMethods.put(packetClass, newList);
        return newList;
    }
    public void registerOnPacketMethod(Class<? extends  Packet> packet, Object bean, Method method){
        var registeredOnPacketMethodList = getRegisteredOnPacketMethodListFor(packet);
        var registeredOnPacketMethod = RegisteredOnPacketMethod.builder().method(method).bean(bean).build();
        if (registeredOnPacketMethodList.contains(registeredOnPacketMethod)){
            return;
        }
        registeredOnPacketMethodList.add(registeredOnPacketMethod);
    }

    public void receivePacket(WebSocketSession webSocketSession, Packet packet){
        var registeredMethods = getRegisteredOnPacketMethodListFor(packet.getClass());
        var galWebsocketSession = GalWebSocketSession.from(webSocketSession);
        for (var registeredOnPacketMethod: registeredMethods){
            registeredOnPacketMethod.invokeMethod(handlerClass, galWebsocketSession, packet);
        }
    }
}
