package com.galsie.gcs.users.data.dto.editprofileinfo.phone.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserPhoneResponseErrorType;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserPhoneRequestDTO {

    @JsonProperty("editing_verification_token")
    @NotNull
    private String editingVerificationToken;

    @JsonProperty("new_phone_verification_token")
    @NotNull
    private String newPhoneVerificationToken;


    public boolean isValidEditingVerificationToken() {
        return this.getEditingVerificationToken().length() > 0;
    }

    public boolean isValidNewPhoneVerificationToken() {
        return this.getNewPhoneVerificationToken().length() > 0;
    }

    public Optional<EditUserInfoVerificationErrorType> validateToken(){
        if(!this.isValidEditingVerificationToken()){
            return Optional.of(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN);
        }
        return Optional.empty();
    }

    public Optional<EditUserPhoneResponseErrorType> validatePhone(){

        if(!this.isValidNewPhoneVerificationToken()){
            return Optional.of(EditUserPhoneResponseErrorType.INVALID_NEW_PHONE_OTP_TOKEN);
        }
        return Optional.empty();
    }
}

