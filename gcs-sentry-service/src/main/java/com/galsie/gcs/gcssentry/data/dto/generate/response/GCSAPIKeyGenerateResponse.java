package com.galsie.gcs.gcssentry.data.dto.generate.response;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.Getter;
import reactor.util.annotation.Nullable;

@GalDTO
@Getter
public class GCSAPIKeyGenerateResponse {

    @Nullable
    GCSAPIKeyGenerateResponseErrorType gcsAPIKeyGenerateResponseError;

    @Nullable
    private String apiKey;

    @Nullable
    private String deviceName;

    private GCSAPIKeyGenerateResponse(String apiKey, String deviceName){
        this.apiKey = apiKey;
        this.deviceName = deviceName;
    }

    private GCSAPIKeyGenerateResponse(GCSAPIKeyGenerateResponseErrorType gcsAPIKeyGenerateResponseError){
        this.gcsAPIKeyGenerateResponseError = gcsAPIKeyGenerateResponseError;
    }

    public static GCSAPIKeyGenerateResponse success(String apiKey, String deviceName){
        return new GCSAPIKeyGenerateResponse(apiKey, deviceName);
    }

    public static GCSAPIKeyGenerateResponse error(GCSAPIKeyGenerateResponseErrorType gcsAPIKeyGenerateResponseError){
        return new GCSAPIKeyGenerateResponse(gcsAPIKeyGenerateResponseError);
    }

    public static GCSResponse<GCSAPIKeyGenerateResponse> responseSuccess(String apiKey, String deviceName){
        return  GCSResponse.response(success(apiKey, deviceName));
    }

    public static GCSResponse<GCSAPIKeyGenerateResponse> responseError(GCSAPIKeyGenerateResponseErrorType errorType){
        return  GCSResponse.response(error(errorType));
    }

}
