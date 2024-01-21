package com.galsie.gcs.microservicecommon.lib.gcsresponse;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseErrorDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetref.AppLangTranslationReferenceDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Very General Errors, usually occur as a result of developers not properly matching requirements.
 * - Act as a layer of protection against the service crashing
 * - Allows the user to realize that there was an issue
 * - Allows presenting details of that issue since GCSResponseErrorType comes coupled with an {@link AppLangTranslationReferenceDTO} in a {@link GCSResponseErrorDTO}
 *
 *
 * JSON Serializable so this works as a DTO.
 **/
@AllArgsConstructor
@Getter
@JsonSerialize(using=GCSResponseErrorTypeSerializer.class)
public enum GCSResponseErrorType {

    NOT_IMPLEMENTED(100, "Not implemented"),
    UNKNOWN_ERROR(101, "Unknown Error"),
    FAILED_TO_SAVE_ENTITY(102, "Failed to save entity"),
    FAILED_TO_DELETE_ENTITY(103, "Failed to delete entity"),
    REPOSITORY_NOT_FOUND(104,"The Repository wasn't found"),
    DUPLICATE_VALUE(105,"Duplicate value exists"),
    ENTITY_NOT_FOUND(106,"Entity not found"),
    MISSING_DATA(107,"Missing Data"),
    NOT_SUPPORTED(108,"Not supported."),
    MISMATCH_ERROR(109,"Mismatch Error"),
    PARSING_ERROR(110,"Parsing Error"),
    TYPE_NOT_FOUND(111,"Type Not Found"),
    /*
    GCS Requests
     */
    GCS_REMOTE_REQUEST_FAILED(112, "GCS Remote Request Failed"),
    GCS_REMOTE_REQUEST_GCS_ERROR(113, "GCS Remote request induced a GCS error"),


    //////////////////////////////////////////////
    /////  AUTHENTICATION: CODES 200 -> 300   ////
    /////////////////////////////////////////////
    INTERNAL_AUTHENTICATION_CONFIGURATION_ERROR(200,"URL expected authentication, but didn't return any configuration related to that."),
    MISSING_AUTHENTICATION_CREDENTIALS(201,"Missing Authentication token credentials"),
    AUTH_SESSION_NOT_FOUND(202,"Auth Session was not found."),
    AUTHENTICATION_SESSION_CREATION_ERROR(203,"Authentication session creation internal error."),

    // API KEY
    API_KEY_EXPIRED(204),  // TODO: Add error messages for these
    API_KEY_INVALID(205),

    // MICROSERVICE
    MICROSERVICE_AUTH_TOKEN_INVALID(206),
    MICROSERVICE_AUTH_SESSION_EXPIRED(207),

    // USER ACCOUNT
    USER_ACCOUNT_NOT_FOUND(208),
    USER_ACCOUNT_DELETED(209),
    USER_ACCOUNT_DISABLED(210),
    USER_AUTH_TOKEN_INVALID(211),
    USER_AUTH_SESSION_EXPIRED(212),
    USER_AUTH_TWO_FA_REQUIRED(213),
    // GALDEVICE
    GALDEVICE_AUTH_TOKEN_INVALID(214),
    GALDEVICE_AUTH_SESSION_EXPIRED(215),
    // TEST
    TEST_AUTH_TOKEN_INVALID(216);

    private int errorCode;
    private String defaultErrorMessage;

    private GCSResponseErrorType(int errorCode){
        this.errorCode = errorCode;
        this.defaultErrorMessage = this.getDefinition();
    }
    public String getDefinition(){
        return this.toString();
    }

    public boolean isAuthRelated() {
        return this.errorCode >= 200 && this.errorCode < 300;
    }
}
