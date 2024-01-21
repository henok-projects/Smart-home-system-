package com.galsie.gcs.homes.data.discrete.addhome;


public enum AddHomeResponseErrorType {
    INVALID_ADDRESS,
    INVALID_HOME_NAME,
    FLOOR_NUMBER_MUST_BE_POSITIVE_NON_ZERO,
    INVALID_HOME_TYPE,
    HOME_NAME_TOO_SHORT,
    HOME_NAME_TOO_LONG
}
