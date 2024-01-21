package com.galsie.gcs.users.data.dto.editprofilephoto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofilephoto.EditProfilePhotoErrorType;
import com.sun.istack.NotNull;
import lombok.*;
import org.apache.commons.codec.binary.Base64;

import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserProfilePhotoRequestDTO {
    @JsonProperty("profile_photo")
    @NotNull
    private String profilePhoto;
    public boolean isValidProfilePhoto() {
        return isValidBase64(profilePhoto);
    }

    public Optional<EditProfilePhotoErrorType> validateProfilePhoto() {
        if (!isValidProfilePhoto()) {
            return Optional.of(EditProfilePhotoErrorType.INVALID_PHOTO_DATA);
        }

        return Optional.empty();
    }
    public Optional<EditProfilePhotoErrorType> validateProfilePhotoSizeInMB() {
        if (profilePhoto.isEmpty()) {
            //TODO  will update the logic based on the discussion , this is just simulation
            return Optional.of(EditProfilePhotoErrorType.PHOTO_SIZE_EXCEEDS_10MB);
        }
        return Optional.empty();
    }
    public Optional<EditProfilePhotoErrorType> validateProfilePhotoAspectRation() {
        if (profilePhoto.isEmpty()) {
            // TODO will update the logic based on the discussion , this is just simulation
            return Optional.of(EditProfilePhotoErrorType.PHOTO_MUST_BE_A_SQUARE);
        }
        return Optional.empty();
    }
    public static boolean isValidBase64(String value) {
        try {
//            Base64.decodeBase64(value); //TODO add base 64 logic this is just simulation
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
