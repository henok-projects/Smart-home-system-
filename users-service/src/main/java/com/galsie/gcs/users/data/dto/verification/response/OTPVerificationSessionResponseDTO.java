package com.galsie.gcs.users.data.dto.verification.response;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.verification.OTPVerificationRequestErrorType;
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
public class OTPVerificationSessionResponseDTO {
    
    @Nullable
    OTPVerificationRequestErrorType otpVerificationRequestError;

    @Nullable
    String otpVerificationToken;

    private static OTPVerificationSessionResponseDTO success(String otpVerificationToken){
        return new OTPVerificationSessionResponseDTO(null, otpVerificationToken);
    }

    private static OTPVerificationSessionResponseDTO error(OTPVerificationRequestErrorType errorType){
        return new OTPVerificationSessionResponseDTO(errorType, null);
    }


    public static GCSResponse<OTPVerificationSessionResponseDTO> successResponse(String otpVerificationToken){
        return GCSResponse.response(success(otpVerificationToken));
    }

    public static GCSResponse<OTPVerificationSessionResponseDTO> errorResponse(OTPVerificationRequestErrorType errorType){
        return GCSResponse.response(error(errorType));
    }
}
