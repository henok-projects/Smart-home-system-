package com.galsie.gcs.users.data.dto.editprofileinfo.fullname.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserFullNameResponseErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserFullNameResponseDTO {

    @JsonProperty("edit_user_info_verification_error")
    @Nullable
    private EditUserInfoVerificationErrorType editUserInfoVerificationError;

    @JsonProperty("edit_user_full_name_error")
    @Nullable
    private EditUserFullNameResponseErrorType editUserFullNameError;

    public static EditUserFullNameResponseDTO error(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserFullNameResponseErrorType editUserFullNameError) {
        return new EditUserFullNameResponseDTO(editUserInfoVerificationErrorType, editUserFullNameError);
    }

    public static EditUserFullNameResponseDTO success() {
        return new EditUserFullNameResponseDTO(null, null);
    }

    public static GCSResponse<EditUserFullNameResponseDTO> responseError(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserFullNameResponseErrorType editUserFullNameError) {
        return GCSResponse.response(error(editUserInfoVerificationErrorType, editUserFullNameError));
    }

    public static GCSResponse<EditUserFullNameResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}
