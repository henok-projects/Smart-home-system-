package com.galsie.gcs.users.data.discrete.editprofileinfo;

public enum EditUserEmailResponseErrorType {
    INVALID_NEW_EMAIL_OTP_TOKEN,

    INVALID_VERIFICATION_ENTITY_ERROR,

    UNVERIFIED_NEW_EMAIL_OTP_TOKEN,
    INACTIVE_NEW_EMAIL_OTP_TOKEN,
    NEW_EMAIL_IS_ASSOCIATED_WITH_ANOTHER_USER
}