package com.galsie.gcs.users.data.dto.removeprofilephoto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.removeProfilePhoto.RemoveProfilePhotoErrorType;
import com.sun.istack.Nullable;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RemoveUserProfilePhotoResponseDTO {

    @Nullable
    @JsonProperty("remove_profile_photo_error")
    RemoveProfilePhotoErrorType removeProfilePhotoError;

    public static RemoveUserProfilePhotoResponseDTO error(RemoveProfilePhotoErrorType removeProfilePhotoErrorType) {
        return new RemoveUserProfilePhotoResponseDTO(removeProfilePhotoErrorType);
    }

    public static RemoveUserProfilePhotoResponseDTO success() {
        return new RemoveUserProfilePhotoResponseDTO();
    }

    public static GCSResponse<RemoveUserProfilePhotoResponseDTO> responseError(RemoveProfilePhotoErrorType removeProfilePhotoErrorType) {
        return GCSResponse.response(error(removeProfilePhotoErrorType));
    }

    public static GCSResponse<RemoveUserProfilePhotoResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}
