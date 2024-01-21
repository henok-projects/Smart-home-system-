package com.galsie.gcs.users.data.dto.editprofileinfo.phone.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserPhoneResponseErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserPhoneResponseDTO {

    @JsonProperty("edit_user_info_verification_error")
    @Nullable
    private EditUserInfoVerificationErrorType editUserInfoVerificationErrorType;

    @JsonProperty("edit_user_phone_response_error")
    @Nullable
    private EditUserPhoneResponseErrorType editUserPhoneResponseErrorType;

    public static EditUserPhoneResponseDTO error(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserPhoneResponseErrorType editUserPhoneResponseErrorType) {
        return new EditUserPhoneResponseDTO(editUserInfoVerificationErrorType, editUserPhoneResponseErrorType);
    }
    public static EditUserPhoneResponseDTO success() {
        return new EditUserPhoneResponseDTO(null, null);
    }

    public static GCSResponse<EditUserPhoneResponseDTO> responseError(EditUserInfoVerificationErrorType editUserInfoVerificationErrorType, EditUserPhoneResponseErrorType editUserPhoneResponseErrorType) {
        return GCSResponse.response(error(editUserInfoVerificationErrorType, editUserPhoneResponseErrorType));
    }

    public static GCSResponse<EditUserPhoneResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}
