package com.galsie.gcs.homes.data.discrete;

/**
 * NOTE: Order of errors is in the order which they are checked for and returned
 */
public enum HomeResponseErrorType {
    //////////////
    // NOTE: this error is accounted for in the DTO related to the request (see usages)
    //////////////
    INVALID_HOME_ID,


    //////////////
    // NOTE: these errors are accounted for in the HomesGalSecurityContextHelper
    //////////////
    HOME_DOESNT_EXIST,
    NOT_PART_OF_THIS_HOME,

    HOME_ISNT_ACTIVE,
    HOME_IS_DELETED,

    // User access
    ACCESS_ENDED,
    LEFT_HOME,

    // User Permissions

    NO_PERMISSION;



}
