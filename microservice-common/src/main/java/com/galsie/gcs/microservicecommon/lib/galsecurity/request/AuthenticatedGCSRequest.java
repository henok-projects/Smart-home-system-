package com.galsie.gcs.microservicecommon.lib.galsecurity.request;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;

import java.lang.annotation.*;

/**
 * This annotation is used by controller endpoints to denote that the request must be authenticated, which types of authentication {@link GalSecurityAuthSessionType
 * is required and whether all or any of the authentication types must be satisfied.
 * @see AuthenticatedGCSRequests
 * @param authenticationStrategy of type {@link AuthenticationStrategy} defines whether all or any of the authentication types must be satisfied
 * if not specified, {@link AuthenticationStrategy#AND} is assumed
 * @param authSessionTypes of type {@link GalSecurityAuthSessionType} defines which authentication types that must specifed in the request headers
 */
@Repeatable(AuthenticatedGCSRequests.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthenticatedGCSRequest {

    AuthenticationStrategy authenticationStrategy() default AuthenticationStrategy.AND;
    GalSecurityAuthSessionType[] authSessionTypes();
}
