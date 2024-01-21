package com.galsie.gcs.users.data.dto.verification.request.resend;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.common.OTPVerificationTokenHolder;
import com.galsie.gcs.users.data.discrete.verification.OTPVerificationType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class ResendOTPVerificationCodeRequestDTO implements OTPVerificationTokenHolder {
    @NotNull
    String otpVerificationToken;

    @Nullable
    OTPVerificationType otpVerificationType; // ignored if otp verification token is not for user. Default is used if nil for the user or if user doesn't have the type

}
