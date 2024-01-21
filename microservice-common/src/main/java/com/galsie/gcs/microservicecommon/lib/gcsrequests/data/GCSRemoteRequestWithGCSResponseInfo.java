package com.galsie.gcs.microservicecommon.lib.gcsrequests.data;

import com.galsie.gcs.microservicecommon.lib.gcsrequests.data.discrete.GCSRemoteRequestErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

@Getter
@Slf4j
public class GCSRemoteRequestWithGCSResponseInfo<T> {

    @Nullable
    private final GCSRemoteRequestErrorType errorType;

    @Nullable
    private final ResponseEntity<GCSResponseDTO<T>> responseEntity;

    private GCSRemoteRequestWithGCSResponseInfo(@Nullable GCSRemoteRequestErrorType errorType, @Nullable ResponseEntity<GCSResponseDTO<T>> responseEntity) {
        this.errorType = errorType;
        this.responseEntity = responseEntity;
    }

    public boolean hasError(){
        return errorType != null;
    }

    public GCSResponse<T> toGCSResponse() {
        if (this.hasError()) {
            log.error("Remote request failed, reason: " + errorType.name());
            return GCSResponse.errorResponse(GCSResponseErrorType.GCS_REMOTE_REQUEST_FAILED);
        }
        var responseBody = this.getResponseEntity().getBody(); // won't be nil since forResponseEntity would be used to initialize this dto
        if (responseBody.hasError()) {
            return GCSResponse.errorResponseWithMessage(GCSResponseErrorType.GCS_REMOTE_REQUEST_GCS_ERROR, responseBody.getError().getErrorType().getDefinition());
        }
        return GCSResponse.response(responseBody.getResponseData());
    }

    public static <T> GCSRemoteRequestWithGCSResponseInfo<T> error(GCSRemoteRequestErrorType gcsRemoteRequestErrorType){
        return new GCSRemoteRequestWithGCSResponseInfo<T>(gcsRemoteRequestErrorType, null);
    }

    public static <T> GCSRemoteRequestWithGCSResponseInfo<T> forResponseEntity(ResponseEntity<GCSResponseDTO<T>> responseEntity){
        if (responseEntity == null){
            GCSRemoteRequestWithGCSResponseInfo.error(GCSRemoteRequestErrorType.RESPONSE_ENTITY_IS_NULL);
        }
        if (responseEntity.getStatusCode() != HttpStatus.OK){
            return GCSRemoteRequestWithGCSResponseInfo.error(GCSRemoteRequestErrorType.HTTP_STATUS_NOT_OK);
        }
        if (responseEntity.getBody() == null){
            return GCSRemoteRequestWithGCSResponseInfo.error(GCSRemoteRequestErrorType.NO_RESPONSE_BODY);
        }
        return new GCSRemoteRequestWithGCSResponseInfo<T>(null, responseEntity);
    }
}
