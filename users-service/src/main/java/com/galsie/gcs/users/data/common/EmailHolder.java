package com.galsie.gcs.users.data.common;

public interface EmailHolder{
    public static String EMAIL_REGEX_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{1,64}){1,2}$";

    String getEmail();


    default boolean isEmailValid(){
        return getEmail().matches(EMAIL_REGEX_PATTERN);
    }
}
