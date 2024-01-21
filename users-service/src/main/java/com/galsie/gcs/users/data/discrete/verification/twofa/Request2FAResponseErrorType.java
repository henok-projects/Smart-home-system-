package com.galsie.gcs.users.data.discrete.verification.twofa;

import com.galsie.gcs.users.data.discrete.verification.OTPVerificationRequestErrorType;

public enum Request2FAResponseErrorType {
    INVALID_EMAIL,
    INVALID_PHONE,
    INVALID_OTP_VERIFICATION_TYPE,
    VERIFICATION_TYPE_NOT_SET_UP,
    TWO_FA_NOT_ENABLED;

    public static Request2FAResponseErrorType fromOTPVerificationErrorType(OTPVerificationRequestErrorType otpVerificationErrorType) {
        return switch (otpVerificationErrorType) {
            case INVALID_EMAIL -> Request2FAResponseErrorType.INVALID_EMAIL;
            case INVALID_PHONE -> Request2FAResponseErrorType.INVALID_PHONE;
            case INVALID_VERIFICATION_TYPE -> Request2FAResponseErrorType.INVALID_OTP_VERIFICATION_TYPE;
            case VERIFICATION_TYPE_NOT_SET_UP -> Request2FAResponseErrorType.VERIFICATION_TYPE_NOT_SET_UP;
        };
    }
}
