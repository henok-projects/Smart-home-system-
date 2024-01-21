package com.galsie.gcs.users.data.dto.editprofileinfo.email.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserEmailResponseErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserEmailRequestDTO {

    @JsonProperty("editing_verification_token")
    @NotNull
    private String editingVerificationToken;

    @JsonProperty("new_email_verification_token")
    @NotNull
    private String newEmailVerificationToken;


    private boolean isValidEditingVerificationToken() {
        return !this.getEditingVerificationToken().isEmpty();
    }

    private boolean isValidNewEmailVerificationToken() {
        return !this.getNewEmailVerificationToken().isEmpty();
    }

    public Optional<EditUserInfoVerificationErrorType> validateEditingVerificationToken(){
        if(!this.isValidEditingVerificationToken()){
            return Optional.of(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN);
        }
        return Optional.empty();
    }

    public Optional<EditUserEmailResponseErrorType> validateEmailVerificationToken(){
        if(!this.isValidNewEmailVerificationToken()){
            return Optional.of(EditUserEmailResponseErrorType.INVALID_NEW_EMAIL_OTP_TOKEN);
        }
        return Optional.empty();
    }

}

