package com.galsie.gcs.users.data.dto.editprofileinfo.twofactorauth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;

import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.usersecuritypin.SetupUserSecurity2FAResponseErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SetupUserSecurity2FAResponseDTO {
    @JsonProperty("edit_user_info_verification_error")
    @Nullable
    private EditUserInfoVerificationErrorType editUserInfoVerificationError;


    @JsonProperty("setup_app_2fa_response_error")
    @Nullable
    private SetupUserSecurity2FAResponseErrorType setupUserSecurity2FAResponseErrorType;

    public static SetupUserSecurity2FAResponseDTO error(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, SetupUserSecurity2FAResponseErrorType setupUserSecurity2FAResponseErrorType) {
        return new SetupUserSecurity2FAResponseDTO(editUserInfoVerificationErrorType, setupUserSecurity2FAResponseErrorType);
    }

    public static SetupUserSecurity2FAResponseDTO success() {
        return new SetupUserSecurity2FAResponseDTO(null, null);
    }

    public static GCSResponse<SetupUserSecurity2FAResponseDTO> responseError(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, SetupUserSecurity2FAResponseErrorType setupUserSecurity2FAResponseErrorType) {
        return GCSResponse.response(error(editUserInfoVerificationErrorType, setupUserSecurity2FAResponseErrorType));
    }

    public static GCSResponse<SetupUserSecurity2FAResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}
