package com.galsie.gcs.homes.data.discrete.homeuseraccess.endaccess;

public enum EndSingleUserHomeAccessResponseErrorType {
    INVALID_USER_ID,
    USER_DOESNT_EXIST,
    USER_NOT_PART_OF_THIS_HOME,
    NO_PERMISSION_TO_END_FOR_THIS_USER,
    ACCESS_ALREADY_ENDED
}