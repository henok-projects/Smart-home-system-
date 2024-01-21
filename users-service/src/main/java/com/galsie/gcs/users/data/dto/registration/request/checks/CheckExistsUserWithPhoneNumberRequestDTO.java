package com.galsie.gcs.users.data.dto.registration.request.checks;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.common.PhoneNumberHolder;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class CheckExistsUserWithPhoneNumberRequestDTO implements PhoneNumberHolder {
    @NotNull
    short countryCode;
    @NotNull
    String phoneNumber;
}
