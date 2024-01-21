package com.galsie.gcs.users.data.dto.editprofileinfo.password.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserPasswordResponseErrorType;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
import lombok.*;

import com.sun.istack.NotNull;
import java.util.Optional;
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditUserPasswordRequestDTO {


    @JsonProperty("editing_verification_token")
    @NotNull
    private String editingVerificationToken;

    @JsonProperty("hashed_pwd")
    @NotNull
    private String hashedPwd;

    public boolean isValidEditingVerificationToken() {
        return !this.getEditingVerificationToken().isEmpty();
    }
    public Optional<EditUserInfoVerificationErrorType> validateToken(){
        if(!this.isValidEditingVerificationToken()){
            return Optional.of(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN);
        }
        return Optional.empty();
    }

    public boolean isHashedPwdValid(){
        return hashedPwd != null && HashingAlgorithm.SHA256.getEncodedLength(64) == hashedPwd.length();
    }

    public Optional<EditUserPasswordResponseErrorType> validatePassword() {
        if (!isHashedPwdValid()) {
            return Optional.of(EditUserPasswordResponseErrorType.INVALID_NEW_PASSWORD);
        }
        return Optional.empty();
    }

}

