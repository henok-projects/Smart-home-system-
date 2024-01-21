package com.galsie.gcs.continuousservice.listener.sockets;

import com.galsie.gcs.continuousservice.gcssockets.handler.TestSocketHandler;
import com.galsie.gcs.continuousservice.gcssockets.packet.packets.SendNamePacket;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.gcssockets.listener.OnPacket;
import com.galsie.gcs.microservicecommon.lib.gcssockets.listener.PacketListener;
import com.galsie.gcs.microservicecommon.lib.gcssockets.GalWebSocketSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@PacketListener(handlerTypes = {TestSocketHandler.class})
public class TestListener {

    /**
     * This method is meant to test if socket(s) handler is provided from the class handler types when a handler type
     * is not specified in the @OnPacket method
     * @param session
     * @param packet
     */
    @OnPacket(handlerTypes = {}, packetTypes = {SendNamePacket.class})
    public void onSendNamePacketHandlerNotSpecified(GalWebSocketSession session, SendNamePacket packet){
       if(session.hasAuthSessionGroup()){
           session.getAuthSessionFor(GalSecurityAuthSessionType.USER).ifPresent(userSession -> {
               log.info("User session found");
           });
       }
       log.info("Received name packet " + packet.getName());
    }

    /**
     * This method tests the ideal scenario when both handler type and packet types are specified
     * @param session
     * @param packet
     */
    @OnPacket(handlerTypes = {TestSocketHandler.class}, packetTypes = {SendNamePacket.class})
    public void onSendNamePacketHandlerSpecified(GalWebSocketSession session, SendNamePacket packet){
        if(session.hasAuthSessionGroup()){
            session.getAuthSessionFor(GalSecurityAuthSessionType.USER).ifPresent(userSession -> {
                log.info("User session found");
            });
        }
        log.info("Received name packet " + packet.getName());
    }
}
