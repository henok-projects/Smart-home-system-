package com.galsie.gcs.microservicecommon.lib.gcssockets.packet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public abstract class Packet {
    public static final String PACKET_ID_JSON_KEY = "packet_id";

    @JsonProperty(PACKET_ID_JSON_KEY)
    private final Long packetId;

    public Packet(AbstractPacketType abstractPacketType){
        this.packetId = abstractPacketType.getId();
    }
}
