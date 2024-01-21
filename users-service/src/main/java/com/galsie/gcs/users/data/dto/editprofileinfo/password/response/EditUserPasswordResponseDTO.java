package com.galsie.gcs.users.data.dto.editprofileinfo.password.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserPasswordResponseErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserPasswordResponseDTO {

    @JsonProperty("edit_user_info_verification_error")
    @Nullable
    private EditUserInfoVerificationErrorType editUserInfoVerificationError;

    @JsonProperty("edit_user_password_error")
    @Nullable
    private EditUserPasswordResponseErrorType editUserPasswordError;

    public static EditUserPasswordResponseDTO error(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserPasswordResponseErrorType editUserPasswordError) {
        return new EditUserPasswordResponseDTO(editUserInfoVerificationErrorType, editUserPasswordError);
    }

    public static EditUserPasswordResponseDTO success() {
        return new EditUserPasswordResponseDTO(null, null);
    }

    public static GCSResponse<EditUserPasswordResponseDTO> responseError(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserPasswordResponseErrorType editUserPasswordError) {
        return GCSResponse.response(error(editUserInfoVerificationErrorType, editUserPasswordError));
    }

    public static GCSResponse<EditUserPasswordResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}

