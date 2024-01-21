package com.galsie.gcs.homes.data.discrete.deleteuserfromrole;

public enum RemoveSingleUserFromRoleResponseErrorType {
    USER_IS_NOT_PART_OF_THIS_HOME,
    USER_IS_NOT_PART_OF_THIS_ROLE,
    CANT_REMOVE_A_USER_FROM_A_ROLE_WITH_MORE_PERMISSIONS_THAN_YOU
}

