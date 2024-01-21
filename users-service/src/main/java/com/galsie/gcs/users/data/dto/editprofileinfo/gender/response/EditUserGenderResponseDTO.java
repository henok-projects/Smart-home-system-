package com.galsie.gcs.users.data.dto.editprofileinfo.gender.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserGenderResponseErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class EditUserGenderResponseDTO {

    @JsonProperty("edit_user_info_verification_error")
    @Nullable
    private EditUserInfoVerificationErrorType editUserInfoVerificationError;

    @JsonProperty("edit_user_gender_error")
    @Nullable
    private EditUserGenderResponseErrorType editUserGenderError;

    public static EditUserGenderResponseDTO error(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserGenderResponseErrorType editUserGenderError) {
        return new EditUserGenderResponseDTO(editUserInfoVerificationErrorType, editUserGenderError);
    }

    public static EditUserGenderResponseDTO success() {
        return new EditUserGenderResponseDTO(null, null);
    }

    public static GCSResponse<EditUserGenderResponseDTO> responseError(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserGenderResponseErrorType editUserGenderError) {
        return GCSResponse.response(error(editUserInfoVerificationErrorType, editUserGenderError));
    }

    public static GCSResponse<EditUserGenderResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}
