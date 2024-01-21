package com.galsie.gcs.users.data.dto.verification.request.twofa;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.verification.OTPVerificationType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAccount2FARequestDTO {
    @NotNull
    OTPVerificationType verificationType;
}
