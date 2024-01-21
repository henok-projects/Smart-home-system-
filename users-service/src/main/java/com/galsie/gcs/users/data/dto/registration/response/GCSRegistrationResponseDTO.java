package com.galsie.gcs.users.data.dto.registration.response;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.users.data.discrete.registration.GCSRegistrationErrorType;
import com.galsie.gcs.users.data.dto.login.response.GalsieLoginResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

/**
 * Either of gcsRegistrationError or gcsLoginData must be nil
 */
@AllArgsConstructor
@Getter
@Setter
@GalDTO
public class GCSRegistrationResponseDTO {

    @Nullable
    GCSRegistrationErrorType registrationErrorType;

    @Nullable
    GalsieLoginResponseDTO loginData;

    public static GCSRegistrationResponseDTO error(GCSRegistrationErrorType registrationErrorType) {
        return new GCSRegistrationResponseDTO(registrationErrorType, null);
    }

    public static GCSRegistrationResponseDTO success(GalsieLoginResponseDTO loginData) {
        return new GCSRegistrationResponseDTO(null, loginData);
    }

    public static GCSResponse<GCSRegistrationResponseDTO> responseError(GCSRegistrationErrorType registrationErrorType) {
        return GCSResponse.response(error(registrationErrorType));
    }

    public static GCSResponse<GCSRegistrationResponseDTO> responseSuccess(GalsieLoginResponseDTO loginData) {
        return GCSResponse.response(success(loginData));
    }
}
