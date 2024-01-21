package com.galsie.gcs.microservicecommon.lib.galsecurity.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to facilitate the use of multiple {@link AuthenticatedGCSRequest} annotations on a single method.
 * in scenarios where multiple {@link AuthenticatedGCSRequest} annotations are used on a single method, the {@link AuthenticationStrategy}
 * is assumed to be {@link AuthenticationStrategy#OR} between the different {@link AuthenticatedGCSRequest} annotations.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedGCSRequests {

    AuthenticatedGCSRequest[] value();

}
