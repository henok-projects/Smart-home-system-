package com.galsie.gcs.users.data.dto.editprofilephoto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofilephoto.EditProfilePhotoErrorType;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserProfilePhotoResponseDTO {

    @Nullable
    @JsonProperty("edit_profile_photo_error")
    private EditProfilePhotoErrorType editProfilePhotoErrorType;

    public static EditUserProfilePhotoResponseDTO error(EditProfilePhotoErrorType editProfilePhotoErrorType) {
        return new EditUserProfilePhotoResponseDTO(editProfilePhotoErrorType);
    }

    public static EditUserProfilePhotoResponseDTO success() {
        return new EditUserProfilePhotoResponseDTO();
    }

    public static GCSResponse<EditUserProfilePhotoResponseDTO> responseError(EditProfilePhotoErrorType editProfilePhotoErrorType) {
        return GCSResponse.response(error(editProfilePhotoErrorType));
    }

    public static GCSResponse<EditUserProfilePhotoResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }

}
