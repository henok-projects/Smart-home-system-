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
@NoArgsConstructor
@Getter
@Setter
public class PerformOTPVerificationResponseDTO {
    
    @Nullable
    OTPVerificationErrorType otpVerificationError;

    @Nullable
    Boolean isValid;

    public static PerformOTPVerificationResponseDTO success(boolean isValid){
        return new PerformOTPVerificationResponseDTO(null, isValid);
    }

    public static PerformOTPVerificationResponseDTO error(OTPVerificationErrorType performOTPVerificationErrorType){
        return new PerformOTPVerificationResponseDTO(performOTPVerificationErrorType, null);
    }

    public static GCSResponse<PerformOTPVerificationResponseDTO> responseError(OTPVerificationErrorType performOTPVerificationErrorType){
        return GCSResponse.response(new PerformOTPVerificationResponseDTO(performOTPVerificationErrorType, null));
    }

    public static GCSResponse<PerformOTPVerificationResponseDTO> responseSuccess(boolean isValid){
        return GCSResponse.response(success(isValid));
    }
}
