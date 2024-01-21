package com.galsie.gcs.users.data.dto.editprofileinfo.username.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.common.UsernameHolder;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserUsernameErrorType;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserUsernameRequestDTO implements UsernameHolder {

    @JsonProperty("editing_verification_token")
    @NotNull
    private String editingVerificationToken;

    @JsonProperty("username")
    @NotNull
    private String username;


    public boolean isValidEditingVerificationToken() {
        return this.getEditingVerificationToken().length() > 0;
    }

    public Optional<EditUserInfoVerificationErrorType> validateToken(){
        if(!this.isValidEditingVerificationToken()){
            return Optional.of(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN);
        }
        return Optional.empty();
    }

    public Optional<EditUserUsernameErrorType> validateUsername(){
        if(!this.isUsernameValid()){
            return Optional.of(EditUserUsernameErrorType.INVALID_NEW_USERNAME_DATA);
        }
        return Optional.empty();
    }
}

