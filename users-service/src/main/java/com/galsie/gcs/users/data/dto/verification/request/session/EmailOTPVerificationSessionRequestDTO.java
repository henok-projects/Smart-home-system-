package com.galsie.gcs.users.data.dto.verification.request.session;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.common.EmailHolder;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GalDTO
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class EmailOTPVerificationSessionRequestDTO implements OTPVerificationSessionRequestDTO, EmailHolder {
    @NotNull
    String email;
}
