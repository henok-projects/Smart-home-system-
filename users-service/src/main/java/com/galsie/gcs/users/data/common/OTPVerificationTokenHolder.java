package com.galsie.gcs.users.data.common;

import com.galsie.gcs.users.data.entity.verification.OTPVerificationSessionEntity;
import com.sun.istack.NotNull;

public interface OTPVerificationTokenHolder {

    String getOtpVerificationToken();

    default boolean isOTPVerificationTokenValid(){
        return getOtpVerificationToken().length() == OTPVerificationSessionEntity.TOKEN_LENGTH;
    }
}
