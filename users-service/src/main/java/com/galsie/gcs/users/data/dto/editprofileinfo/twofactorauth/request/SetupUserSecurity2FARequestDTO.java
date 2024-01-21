package com.galsie.gcs.users.data.dto.editprofileinfo.twofactorauth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.usersecuritypin.SetupUserSecurity2FAResponseErrorType;
import lombok.*;

import com.sun.istack.NotNull;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SetupUserSecurity2FARequestDTO {


    @JsonProperty("editing_verification_token")
    @NotNull
    private String editingVerificationToken;


    @JsonProperty("enable2FA")
    @NotNull
    private Boolean enable2FA;

    public boolean isValidEditingVerificationToken() {
        return this.getEditingVerificationToken().length() > 0;
    }
    public Optional<EditUserInfoVerificationErrorType> validateToken(){
        if(!this.isValidEditingVerificationToken()){
            return Optional.of(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN);
        }
        return Optional.empty();
    }
    public boolean isEnable2FAValid() {
        return this.enable2FA instanceof Boolean; //2FA is a non-null boolean, no error
    }
    public Optional<SetupUserSecurity2FAResponseErrorType> validate2FAEnabled() {
        if (isEnable2FAValid()) {
            return Optional.empty(); //
        } else {
            return Optional.of(SetupUserSecurity2FAResponseErrorType.INVALID_2FA_VALUE);
        }
    }


}
