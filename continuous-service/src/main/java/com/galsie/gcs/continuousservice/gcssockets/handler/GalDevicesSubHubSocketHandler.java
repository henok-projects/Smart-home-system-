package com.galsie.gcs.continuousservice.gcssockets.handler;

import com.galsie.gcs.microservicecommon.lib.gcssockets.handler.GCSSocketHandler;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class GalDevicesSubHubSocketHandler extends GCSSocketHandler {


    BiMap<String, WebSocketSession> galDevicesSubhubSessions = HashBiMap.create();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var mobileId = (String)session.getAttributes().get("mobileId");
        galDevicesSubhubSessions.put(mobileId, session);
        log.info("Gal Device subhub session established");
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var mobileId = galDevicesSubhubSessions.inverse().remove(session);
        log.info("Gal Device subhub session closed for mobile id " + mobileId);
    }

}
