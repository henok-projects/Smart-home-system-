package com.galsie.gcs.microservicecommon.lib.gcssockets.handler;

import com.galsie.gcs.microservicecommon.lib.gcssockets.listener.registry.GCSGlobalPacketListenerRegistry;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.Packet;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.serializer.GCSPacketSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;


/**
 *
 * NOTE: When a {@link com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionGroup} (or frankly any session) is set in the {@link org.springframework.security.core.context.SecurityContext},
 *      the {@link WebSocketSession} holds that session as its principal. So it can be gotten through {@link WebSocketSession#getPrincipal()}
 */
@NoArgsConstructor
@Slf4j
public abstract class GCSSocketHandler extends TextWebSocketHandler {

    /**
     * Note: gcsPacketSerializer and gcsGlobalPacketListenerRegistry are injected by {@link com.galsie.gcs.microservicecommon.lib.gcssockets.GCSWebSocketsConfiguration}
     */
    public GCSPacketSerializer gcsPacketSerializer;
    public GCSGlobalPacketListenerRegistry gcsGlobalPacketListenerRegistry;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        super.afterConnectionEstablished(session);
        log.info("New connection established for the handler " +  this.getHandlerName() + ". Session id " + session.getId());

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        log.warn("Transport error for the handler " + this.getHandlerName() + ". Session id" + session.getId() + ". Error: " + exception.getLocalizedMessage());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        super.handleTextMessage(session, message);
        log.info("New text message from client with id " + session.getId() + ": "  + message.getPayload());
        String json = message.getPayload();
        Optional<Packet> packetOpt = gcsPacketSerializer.deserialize(json);
        if (packetOpt.isEmpty()){
            log.error("Failed to receive a packet for the handler " + this.getHandlerName() + ": Deserialization failed for the json " + json);
            return;
        }
        Packet packet = packetOpt.get();
        gcsGlobalPacketListenerRegistry.receivePacket(this.getClass(), session, packet);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        super.afterConnectionClosed(session, status);
        log.info("Connection lost for the handler " +  this.getHandlerName() + ". Session id " + session.getId() + ", reason: " + status.getReason());
    }

    private String getHandlerName(){
        return this.getClass().getName();
    }
}
