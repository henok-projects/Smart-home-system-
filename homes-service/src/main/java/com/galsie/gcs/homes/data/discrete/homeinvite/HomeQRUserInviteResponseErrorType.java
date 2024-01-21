package com.galsie.gcs.homes.data.discrete.homeinvite;

public enum HomeQRUserInviteResponseErrorType {
    /**
     * Used for home direct user invites
     *
     */
    USER_NOT_FOUND,
    /**
     * Used for home direct user invites & qr code invites
     *
     */
    INVALID_DATES,
    START_DATE_MUST_PRECEDE_END_DATE,
    NO_PERMISSION,
    INVITE_CODE_GENERATION_REACHED_MAX_RETRIES
}

