package com.galsie.gcs.users.data.dto.editprofileinfo.email.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserEmailResponseErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserEmailResponseDTO {

    @JsonProperty("edit_user_info_verification_error")
    @Nullable
    private EditUserInfoVerificationErrorType editUserInfoVerificationErrorType;

    @JsonProperty("edit_user_email_response_error")
    @Nullable
    private EditUserEmailResponseErrorType editUserEmailResponseErrorType;

    public static EditUserEmailResponseDTO error(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserEmailResponseErrorType editUserEmailResponseErrorType) {
        return new EditUserEmailResponseDTO(editUserInfoVerificationErrorType, editUserEmailResponseErrorType);
    }
    public static EditUserEmailResponseDTO success() {
        return new EditUserEmailResponseDTO(null, null);
    }

    public static GCSResponse<EditUserEmailResponseDTO> responseError(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserEmailResponseErrorType editUserEmailResponseErrorType) {
        return GCSResponse.response(error(editUserInfoVerificationErrorType, editUserEmailResponseErrorType));
    }

    public static GCSResponse<EditUserEmailResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}
