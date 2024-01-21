package com.galsie.gcs.microservicecommon.lib.gcssockets.packet;

public interface AbstractPacketType {


    Long getId();
    Class<? extends Packet> getPacketClass();
}
