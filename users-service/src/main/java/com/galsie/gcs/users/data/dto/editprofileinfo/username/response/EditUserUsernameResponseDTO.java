package com.galsie.gcs.users.data.dto.editprofileinfo.username.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserUsernameErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserUsernameResponseDTO {

    @JsonProperty("edit_user_info_verification_error")
    @Nullable
    private EditUserInfoVerificationErrorType editUserInfoVerificationError;

    @JsonProperty("edit_user_username_error")
    @Nullable
    private EditUserUsernameErrorType editUserUsernameError;

    public static EditUserUsernameResponseDTO error(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserUsernameErrorType editUserUsernameErrorType) {
        return new EditUserUsernameResponseDTO(editUserInfoVerificationErrorType, editUserUsernameErrorType);
    }
    public static EditUserUsernameResponseDTO success() {
        return new EditUserUsernameResponseDTO(null, null);
    }

    public static GCSResponse<EditUserUsernameResponseDTO> responseError(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserUsernameErrorType editUserUsernameErrorType) {
        return GCSResponse.response(error(editUserInfoVerificationErrorType, editUserUsernameErrorType));
    }

    public static GCSResponse<EditUserUsernameResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}
