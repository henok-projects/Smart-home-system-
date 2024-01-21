package com.galsie.gcs.microservicecommon.lib.galsecurity.request;

/**
 * This enum is used to define whether all or any of the authentication types must be satisfied in an {@link AuthenticatedGCSRequest}.
 */
public enum AuthenticationStrategy {

    AND,
    OR

}
