package com.galsie.gcs.microservicecommon.lib.gcssockets.listener;

import com.galsie.gcs.microservicecommon.lib.gcssockets.handler.GCSSocketHandler;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.Packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface OnPacket {

    Class<? extends GCSSocketHandler>[] handlerTypes();

    Class<? extends Packet>[] packetTypes();
}
