package com.galsie.gcs.users.data.dto.login.response;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.users.data.discrete.login.GalsieLoginResponseErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GalsieLoginResponseDTO {

    @Nullable
    GalsieLoginResponseErrorType loginResponseError;

    @Nullable
    GalsieLoginDataDTO loginData;

    public static GalsieLoginResponseDTO error(GalsieLoginResponseErrorType loginResponseErrorType) {
        return new GalsieLoginResponseDTO(loginResponseErrorType, null);
    }

    public static GalsieLoginResponseDTO success(GalsieLoginDataDTO loginData) {
        return new GalsieLoginResponseDTO(null, loginData);
    }

    public static GCSResponse<GalsieLoginResponseDTO> responseError(GalsieLoginResponseErrorType loginResponseErrorType) {
        return GCSResponse.response(error(loginResponseErrorType));
    }

    public static GCSResponse<GalsieLoginResponseDTO> responseSuccess(GalsieLoginDataDTO loginData) {
        return GCSResponse.response(success(loginData));
    }
}
