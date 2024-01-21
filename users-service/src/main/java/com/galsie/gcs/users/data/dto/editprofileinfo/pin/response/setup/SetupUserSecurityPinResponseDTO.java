package com.galsie.gcs.users.data.dto.editprofileinfo.pin.response.setup;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.usersecuritypin.SetupUserSecurityAppPinResponseErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SetupUserSecurityPinResponseDTO {

    @JsonProperty("edit_user_info_verification_error")
    @Nullable
    private EditUserInfoVerificationErrorType editUserInfoVerificationError;

    @Nullable
    @JsonProperty("setup_app_pin_response_error")
    private SetupUserSecurityAppPinResponseErrorType setupUserSecurityAppPinResponseErrorType;

    public static SetupUserSecurityPinResponseDTO error(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, SetupUserSecurityAppPinResponseErrorType setupUserSecurityAppPinResponseErrorType) {
        return new SetupUserSecurityPinResponseDTO(editUserInfoVerificationErrorType, setupUserSecurityAppPinResponseErrorType);
    }

    public static SetupUserSecurityPinResponseDTO success() {
        return new SetupUserSecurityPinResponseDTO(null, null);
    }

    public static GCSResponse<SetupUserSecurityPinResponseDTO> responseError(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, SetupUserSecurityAppPinResponseErrorType setupUserSecurityAppPinResponseErrorType) {
        return GCSResponse.response(error(editUserInfoVerificationErrorType, setupUserSecurityAppPinResponseErrorType));
    }

    public static GCSResponse<SetupUserSecurityPinResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }

}


