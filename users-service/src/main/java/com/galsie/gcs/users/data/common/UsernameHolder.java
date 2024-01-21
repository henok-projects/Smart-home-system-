package com.galsie.gcs.users.data.common;

public interface UsernameHolder {
    static String USERNAME_REGEX = "[a-zA-Z0-9_\\-\\.]{3,12}";

    String getUsername();

    default boolean isUsernameValid(){
        return this.getUsername().matches(USERNAME_REGEX);
    }
}
