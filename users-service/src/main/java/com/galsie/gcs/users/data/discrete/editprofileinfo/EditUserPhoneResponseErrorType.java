package com.galsie.gcs.users.data.discrete.editprofileinfo;

public enum EditUserPhoneResponseErrorType {
    INVALID_NEW_PHONE_OTP_TOKEN,
    UNVERIFIED_NEW_PHONE_OTP_TOKEN,
    INACTIVE_NEW_PHONE_OTP_TOKEN,
    INVALID_VERIFICATION_ENTITY_ERROR, NEW_PHONE_IS_ASSOCIATED_WITH_ANOTHER_USER
}