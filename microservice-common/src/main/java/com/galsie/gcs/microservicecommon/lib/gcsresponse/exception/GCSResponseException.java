package com.galsie.gcs.microservicecommon.lib.gcsresponse.exception;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseErrorDTO;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
Thrown for the sake of rollback transactions while still being able to get the GCSResponse by the calling method
 -- Extends RuntimeException for transactions to rollback when this is thrown
 */
@Getter
public class GCSResponseException extends RuntimeException{
    private final GCSResponse gcsResponse;
    public <T> GCSResponseException(GCSResponse<T> gcsResponse){
        this.gcsResponse = gcsResponse;
    }

    public <T> GCSResponseException(GCSResponseErrorDTO errorDTO){
        this(GCSResponse.errorResponse(errorDTO));
    }
    public <T> GCSResponseException(GCSResponseErrorType errorType){
        this(GCSResponse.errorResponse(errorType));
    }
    public <T> GCSResponseException(GCSResponseErrorType errorType, String errorMessage){
        this(GCSResponse.errorResponseWithMessage(errorType, errorMessage));
    }

    public <T> GCSResponse<T> getGcsResponse(Class<T> tClass){
        return (GCSResponse<T>) this.gcsResponse;
    }

    public GCSResponseErrorDTO getGcsError() {
        return gcsResponse.getGcsError();
    }
}
