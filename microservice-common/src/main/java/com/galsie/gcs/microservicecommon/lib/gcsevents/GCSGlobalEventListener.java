package com.galsie.gcs.microservicecommon.lib.gcsevents;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Classes annotated with GCSGlobalEventListener must conform to {@link com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventListener}
 * - NOTE: {@link com.galsie.gcs.microservicecommon.config.componentscan.ComponentScanConfiguration} is configured to create beans for all GCSGlobalEventListener annotated classes, so no need to annotate with @Component
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GCSGlobalEventListener {
}
