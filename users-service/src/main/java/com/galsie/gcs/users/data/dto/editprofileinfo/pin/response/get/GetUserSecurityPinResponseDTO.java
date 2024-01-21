package com.galsie.gcs.users.data.dto.editprofileinfo.pin.response.get;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.usersecuritypin.GetUserSecurityAppPinResponseErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetUserSecurityPinResponseDTO {

    @JsonProperty("pin_setup_response_error")
    @Nullable
    private GetUserSecurityAppPinResponseErrorType getPinResponseError;

    @JsonProperty("hashed_pin")
    @Nullable
    private String hashedPin;

    public static GetUserSecurityPinResponseDTO error(GetUserSecurityAppPinResponseErrorType userSecurityGetPinResponseError) {
        return new GetUserSecurityPinResponseDTO(userSecurityGetPinResponseError, null);
    }

    public static GetUserSecurityPinResponseDTO success(String hashedPin) {
        return new GetUserSecurityPinResponseDTO(null, hashedPin);
    }

    public static GCSResponse<GetUserSecurityPinResponseDTO> responseError(GetUserSecurityAppPinResponseErrorType userSecurityGetPinResponseError) {
        return GCSResponse.response(error(userSecurityGetPinResponseError));
    }

    public static GCSResponse<GetUserSecurityPinResponseDTO> responseSuccess(String hashedPin) {
        return GCSResponse.response(success(hashedPin));
    }

}

