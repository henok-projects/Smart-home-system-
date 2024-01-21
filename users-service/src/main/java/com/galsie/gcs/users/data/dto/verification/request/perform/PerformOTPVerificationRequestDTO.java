package com.galsie.gcs.users.data.dto.verification.request.perform;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.common.OTPVerificationTokenHolder;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
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
public class PerformOTPVerificationRequestDTO implements OTPVerificationTokenHolder {
    @NotNull
    String otpVerificationToken;

    @NotNull
    String hashedOTP;

    public boolean isHashedOTPValid() {
        return hashedOTP.length() == HashingAlgorithm.SHA256.getEncodedLength(64);
    }

}
