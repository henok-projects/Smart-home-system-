package com.galsie.gcs.users.data.discrete.authentication;

public enum UserAccountAuthenticationResponseErrorType {
    INVALID_TOKEN,
    SESSION_EXPIRED,
    ACCOUNT_NOT_FOUND,
    ACCOUNT_DELETED,
    ACCOUNT_DISABLED,
    TWO_FA_REQUIRED;
}
