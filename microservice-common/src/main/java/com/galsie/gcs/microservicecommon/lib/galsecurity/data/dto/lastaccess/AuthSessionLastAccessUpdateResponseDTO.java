package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.parameters.P;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthSessionLastAccessUpdateResponseDTO {
    @Nullable
    AuthSessionLastUpdateResponseErrorType authSessionLastUpdateResponseError;

    public boolean hasError(){
        return authSessionLastUpdateResponseError != null;
    }

    public static GCSResponse<AuthSessionLastAccessUpdateResponseDTO> responseSuccess(){
        return GCSResponse.response(new AuthSessionLastAccessUpdateResponseDTO());
    }

    public static GCSResponse<AuthSessionLastAccessUpdateResponseDTO> responseError(AuthSessionLastUpdateResponseErrorType errorType){
        return GCSResponse.response(new AuthSessionLastAccessUpdateResponseDTO(errorType));
    }
}
