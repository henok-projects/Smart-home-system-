package com.galsie.gcs.microservicecommon.lib.gcssockets;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionGroup;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

/**
 * When a {@link GalSecurityAuthSessionGroup} is set into spring security's context using a request filter on the websockets handshake,
 * the session group would be stored in the principal of the {@link WebSocketSession}
 * this class exists for the sake of making it more convenient to access the session group
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class GalWebSocketSession {

    @Nullable
    GalSecurityAuthSessionGroup securityAuthSessionGroup;

    @NotNull
    WebSocketSession webSocketSession;

    private GalWebSocketSession(WebSocketSession webSocketSession, @Nullable GalSecurityAuthSessionGroup securityAuthSessionGroup){
        this.webSocketSession = webSocketSession;
        this.securityAuthSessionGroup = (GalSecurityAuthSessionGroup) webSocketSession.getPrincipal();
    }

    public Optional<WebSocketSession> getWebSocketSessionOpt(){
        return Optional.of(webSocketSession);
    }

    public Boolean hasAuthSessionGroup(){
        return securityAuthSessionGroup != null;
    }

    public Optional<GalSecurityAuthSessionGroup> getAuthSessionGroupOpt(){
        return Optional.of(securityAuthSessionGroup);
    }

    public Optional<GalSecurityAuthSession> getAuthSessionFor(GalSecurityAuthSessionType type) {
        if (!hasAuthSessionGroup()) {
            return Optional.empty();
        }
        var authSessionOpt = securityAuthSessionGroup.getGalSecurityAuthSessionFor(type);
        if (authSessionOpt.isEmpty()) {
            log.info("No auth session found for type " + type);
            return Optional.empty();
        }
        return Optional.of(authSessionOpt.get());
    }

    public static GalWebSocketSession from(WebSocketSession webSocketSession){
        if(webSocketSession.getPrincipal() instanceof  GalSecurityAuthSessionGroup galSecurityAuthSessionGroup){
            return new GalWebSocketSession(webSocketSession, galSecurityAuthSessionGroup);
        }
        return new GalWebSocketSession(webSocketSession, null);
    }

}
