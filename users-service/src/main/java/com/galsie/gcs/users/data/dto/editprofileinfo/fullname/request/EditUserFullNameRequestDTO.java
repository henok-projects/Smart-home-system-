package com.galsie.gcs.users.data.dto.editprofileinfo.fullname.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserFullNameResponseErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import lombok.*;
import reactor.util.annotation.Nullable;

import com.sun.istack.NotNull;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Builder
public class EditUserFullNameRequestDTO {

    @JsonProperty("editing_verification_token")
    @NotNull
    private String editingVerificationToken;

    @JsonProperty("first_name")
    @NotNull
    String firstName;


    @JsonProperty("last_name")
    @NotNull
    String lastName;


    @JsonProperty("middle_name")
    @Nullable
    String middleName;

    public boolean isValidEditingVerificationToken() {
        return !this.getEditingVerificationToken().isEmpty();
    }

    public boolean isValidFirstName() {
        return !this.getFirstName().isEmpty();
    }
    public boolean isValidLastName() {
        return !this.getLastName().isEmpty();
    }
    public boolean isValidMiddleName() {
        if(this.getMiddleName() == null){
            return true;
        }
        return !this.getMiddleName().isEmpty();
    }

    public Optional<EditUserInfoVerificationErrorType> validateEditingVerificationToken(){
        if(!this.isValidEditingVerificationToken()){
            return Optional.of(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN);
        }
        return Optional.empty();
    }

    public Optional<EditUserFullNameResponseErrorType> validateFullName(){
        if(!this.isValidFirstName()){
            return Optional.of(EditUserFullNameResponseErrorType.INVALID_NEW_FIRST_NAME);
        }
        if(!this.isValidLastName()){
            return Optional.of(EditUserFullNameResponseErrorType.INVALID_NEW_LAST_NAME);
        }
        if(!this.isValidMiddleName()){
            return Optional.of(EditUserFullNameResponseErrorType.INVALID_NEW_MIDDLE_NAME);
        }
        return Optional.empty();
    }

    public Optional<EditUserFullNameResponseErrorType> validateFirstName(){
        if(!this.isValidFirstName()){
            return Optional.of(EditUserFullNameResponseErrorType.INVALID_NEW_FIRST_NAME);
        }
        return Optional.empty();
    }
    public Optional<EditUserFullNameResponseErrorType> validateLastName(){
        if(!this.isValidLastName()){
            return Optional.of(EditUserFullNameResponseErrorType.INVALID_NEW_LAST_NAME);
        }
        return Optional.empty();
    }
    public Optional<EditUserFullNameResponseErrorType> validateMiddleName(){
        if(!this.isValidMiddleName()){
            return Optional.of(EditUserFullNameResponseErrorType.INVALID_NEW_MIDDLE_NAME);
        }
        return Optional.empty();
    }
}
