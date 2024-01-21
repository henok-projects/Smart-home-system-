package com.galsie.gcs.users.data.dto.editprofileinfo.dateofbirth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserBirthdateResponseErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserBirthdateResponseDTO {

    @JsonProperty("edit_user_info_verification_error")
    @Nullable
    private EditUserInfoVerificationErrorType editUserInfoVerificationError;

    @JsonProperty("edit_user_birthdate_error")
    @Nullable
    private EditUserBirthdateResponseErrorType editUserBirthdateError;

    public static EditUserBirthdateResponseDTO error(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserBirthdateResponseErrorType editUserBirthdateError) {
        return new EditUserBirthdateResponseDTO(editUserInfoVerificationErrorType, editUserBirthdateError);
    }

    public static EditUserBirthdateResponseDTO success() {
        return new EditUserBirthdateResponseDTO(null, null);
    }

    public static GCSResponse<EditUserBirthdateResponseDTO> responseError(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserBirthdateResponseErrorType editUserBirthdateError) {
        return GCSResponse.response(error(editUserInfoVerificationErrorType, editUserBirthdateError));
    }

    public static GCSResponse<EditUserBirthdateResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}
