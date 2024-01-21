package com.galsie.gcs.users.data.dto.editprofileinfo.gender.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.UserGender;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserGenderResponseErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import lombok.*;

import com.sun.istack.NotNull;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserGenderRequestDTO {

    @JsonProperty("editing_verification_token")
    @NotNull
    private String editingVerificationToken;


    @JsonProperty("gender")
    @NotNull
    private String gender;

    public boolean isValidEditingVerificationToken() {
        return !this.getEditingVerificationToken().isEmpty();
    }
    public Optional<EditUserInfoVerificationErrorType> validateToken(){
        if(!this.isValidEditingVerificationToken()){
            return Optional.of(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN);
        }
        return Optional.empty();
    }

    public boolean isValidGender() {
        return UserGender.fromString(gender).isPresent();
    }

    public Optional<EditUserGenderResponseErrorType> validateGender() {
        if (!isValidGender()) {
            return Optional.of(EditUserGenderResponseErrorType.INVALID_NEW_GENDER_DATA);
        }
        return Optional.empty();
    }

}
