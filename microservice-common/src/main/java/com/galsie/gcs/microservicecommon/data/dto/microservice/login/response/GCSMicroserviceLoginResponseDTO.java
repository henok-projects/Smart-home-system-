package com.galsie.gcs.microservicecommon.data.dto.microservice.login.response;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GCSMicroserviceLoginResponseDTO {
    @Nullable
    GCSMicroserviceLoginResponseErrorType gcsMicroserviceLoginResponseError;

    @Nullable
    String microserviceAuthToken;


    public boolean hasError(){
        return gcsMicroserviceLoginResponseError != null;
    }

    public static GCSResponse<GCSMicroserviceLoginResponseDTO> responseError(GCSMicroserviceLoginResponseErrorType errorType){
        return GCSResponse.response(new GCSMicroserviceLoginResponseDTO(errorType, null));
    }

    public static GCSResponse<GCSMicroserviceLoginResponseDTO> responseSuccess(String microserviceAuthToken){
        return GCSResponse.response(new GCSMicroserviceLoginResponseDTO(null, microserviceAuthToken));
    }
}
