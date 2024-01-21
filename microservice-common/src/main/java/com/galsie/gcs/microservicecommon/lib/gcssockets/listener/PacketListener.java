package com.galsie.gcs.microservicecommon.lib.gcssockets.listener;

import com.galsie.gcs.microservicecommon.lib.gcssockets.handler.GCSSocketHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate classes the contain methods annotated with {@link OnPacket} and provide a deafault handler type(s) for the methods
 * that do not have them specified
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface PacketListener {

    Class<? extends GCSSocketHandler>[] handlerTypes();

}
