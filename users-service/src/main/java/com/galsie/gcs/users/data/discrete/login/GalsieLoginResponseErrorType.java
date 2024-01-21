package com.galsie.gcs.users.data.discrete.login;

public enum GalsieLoginResponseErrorType {
    INVALID_CREDENTIALS,
    FAILED_SENDING_2FA_OTP,
    ACCOUNT_DELETED,
    ACCOUNT_DISABLED,
    LOGIN_CANCELLED;
}
