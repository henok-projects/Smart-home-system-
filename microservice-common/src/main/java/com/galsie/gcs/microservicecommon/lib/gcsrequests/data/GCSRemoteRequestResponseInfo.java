package com.galsie.gcs.microservicecommon.lib.gcsrequests.data;

import com.galsie.gcs.microservicecommon.lib.gcsrequests.data.discrete.GCSRemoteRequestErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

@Getter
public class GCSRemoteRequestResponseInfo<T> {
    @Nullable
    private final GCSRemoteRequestErrorType errorType;

    @Nullable
    private final ResponseEntity<T> responseEntity;

    public GCSRemoteRequestResponseInfo(@Nullable GCSRemoteRequestErrorType errorType, @Nullable ResponseEntity<T> responseEntity) {
        this.errorType = errorType;
        this.responseEntity = responseEntity;
    }

    public boolean hasError(){
        return errorType != null;
    }

    public GCSResponse<T> toGCSResponse(){
        if (this.hasError()){
            return GCSResponse.errorResponse(GCSResponseErrorType.GCS_REMOTE_REQUEST_FAILED);
        }
        return GCSResponse.response(responseEntity.getBody()); // body wouldn't be nil since instance created using forResponseEntity
    }

    public static <T> GCSRemoteRequestResponseInfo<T> error(GCSRemoteRequestErrorType gcsRemoteRequestErrorType){
        return new GCSRemoteRequestResponseInfo<T>(gcsRemoteRequestErrorType, null);
    }

    public static <T> GCSRemoteRequestResponseInfo<T> forResponseEntity(ResponseEntity<T> responseEntity){
        if (responseEntity == null){
            return GCSRemoteRequestResponseInfo.error(GCSRemoteRequestErrorType.RESPONSE_ENTITY_IS_NULL);
        }
        if (responseEntity.getStatusCode() != HttpStatus.OK){
            return GCSRemoteRequestResponseInfo.error(GCSRemoteRequestErrorType.HTTP_STATUS_NOT_OK);
        }
        if (responseEntity.getBody() == null){
            return GCSRemoteRequestResponseInfo.error(GCSRemoteRequestErrorType.NO_RESPONSE_BODY);
        }
        return new GCSRemoteRequestResponseInfo<>(null, responseEntity);
    }
}
