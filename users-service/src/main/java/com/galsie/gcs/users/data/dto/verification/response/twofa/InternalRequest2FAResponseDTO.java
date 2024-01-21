package com.galsie.gcs.users.data.dto.verification.response.twofa;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.users.data.discrete.verification.twofa.Request2FAResponseErrorType;
import com.galsie.gcs.users.data.entity.security.UserAuthSessionEntity;
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
public class InternalRequest2FAResponseDTO {

    @Nullable
    Request2FAResponseErrorType request2FAResponseError;

    @Nullable
    UserAuthSessionEntity userAuthSessionEntity;

    public static GCSResponse<InternalRequest2FAResponseDTO> responseError(Request2FAResponseErrorType request2FAResponseError){
        return GCSResponse.response(new InternalRequest2FAResponseDTO(request2FAResponseError, null));
    }

    public static GCSResponse<InternalRequest2FAResponseDTO> responseSuccess(UserAuthSessionEntity userAuthSessionEntity){
        return GCSResponse.response(new InternalRequest2FAResponseDTO(null, userAuthSessionEntity));
    }
}
