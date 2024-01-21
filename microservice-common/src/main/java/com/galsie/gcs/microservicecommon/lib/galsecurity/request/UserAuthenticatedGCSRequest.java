package com.galsie.gcs.microservicecommon.lib.galsecurity.request;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used by controller endpoints to denote that the request must be authenticated in particular way,
 * with a {@link GalSecurityAuthSessionType#USER} and a {@link GalSecurityAuthSessionType#GCS_API_CLIENT} session and a {@link AuthenticationStrategy#AND}.
 * is required and whether all or any of the authentication types must be satisfied.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserAuthenticatedGCSRequest {
}
