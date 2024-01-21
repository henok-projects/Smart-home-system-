package com.galsie.gcs.users.data.dto.editprofileinfo.pin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.usersecuritypin.SetupUserSecurityAppPinResponseErrorType;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
import com.sun.istack.NotNull;
import lombok.*;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SetupUserSecurityAppPinRequestDTO {

    @JsonProperty("editing_verification_token")
    @NotNull
    private String editingVerificationToken;


    @JsonProperty("hashed_pin")
    @NotNull
    private String hashedPin;

    public boolean isValidEditingVerificationToken() {
        return !this.getEditingVerificationToken().isEmpty();
    }
    public Optional<SetupUserSecurityAppPinResponseErrorType> validateToken(){
        if(!this.isValidEditingVerificationToken()){
            return Optional.of(SetupUserSecurityAppPinResponseErrorType.INVALID_PIN);
        }
        return Optional.empty();
    }
    public boolean isHashedPwdValid(){
        return hashedPin != null && HashingAlgorithm.SHA256.getEncodedLength(64) == hashedPin.length();
    }
    public Optional<SetupUserSecurityAppPinResponseErrorType> validateHashedPin() {
        if (!isHashedPwdValid()) {
            return Optional.of(SetupUserSecurityAppPinResponseErrorType.INVALID_PIN);
        }
        return Optional.empty();
    }

}

