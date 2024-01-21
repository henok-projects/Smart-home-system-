package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
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
public class AuthenticationResponseDTO {
    @Nullable
    GCSResponseErrorType authenticationResponseError;

    public boolean isAuthenticated(){
        return authenticationResponseError == null;
    }

    public static GCSResponse<AuthenticationResponseDTO> responseError(GCSResponseErrorType authenticationResponseErrorType){
        return GCSResponse.response(new AuthenticationResponseDTO(authenticationResponseErrorType));
    }

    public static GCSResponse<AuthenticationResponseDTO> responseSuccess(){
        return GCSResponse.response(new AuthenticationResponseDTO());
    }

}
