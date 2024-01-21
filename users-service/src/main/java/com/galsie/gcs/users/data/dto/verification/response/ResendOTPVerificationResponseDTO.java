package com.galsie.gcs.users.data.dto.verification.response;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.users.data.discrete.verification.OTPVerificationErrorType;
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
public class ResendOTPVerificationResponseDTO{
    
    @Nullable
    OTPVerificationErrorType otpVerificationErrorType;


    public static GCSResponse<ResendOTPVerificationResponseDTO> responseError(OTPVerificationErrorType otpVerificationErrorType) {
        return GCSResponse.response(new ResendOTPVerificationResponseDTO(otpVerificationErrorType));
    }
    public static GCSResponse<ResendOTPVerificationResponseDTO> responseSuccess() {
        return GCSResponse.response(new ResendOTPVerificationResponseDTO(null));
    }

}
