package com.galsie.gcs.users.data.dto.editprofileinfo.dateofbirth.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserBirthdateResponseErrorType;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.sun.istack.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Builder
public class EditUserBirthdateRequestDTO {


    @JsonProperty("editing_verification_token")
    @NotNull
    private String editingVerificationToken;

    @JsonProperty("birth_date")
    @NotNull
    private String birthDate;

    public boolean isValidEditingVerificationToken() {
        return !this.getEditingVerificationToken().isEmpty();
    }

    public Optional<EditUserInfoVerificationErrorType> validateToken(){
        if(!this.isValidEditingVerificationToken()){
            return Optional.of(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN);
        }
        return Optional.empty();
    }

    public boolean isValidBirthdate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // Attempt to parse the birthDate string in the expected format
            var parsedDate = LocalDate.parse(birthDate, formatter);

            // Get the current date
            var currentDate = LocalDate.now();
            return !parsedDate.isAfter(currentDate);
        } catch (DateTimeParseException e) {
            // Date format parsing failed
            return false;
        }
    }

    public Optional<EditUserBirthdateResponseErrorType> validateBirthdate() {
        if (!this.isValidBirthdate()) {
            return Optional.of(EditUserBirthdateResponseErrorType.INVALID_NEW_BIRTHDATE_DATA);
        }
        return Optional.empty();
    }

}