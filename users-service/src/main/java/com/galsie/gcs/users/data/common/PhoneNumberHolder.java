package com.galsie.gcs.users.data.common;

public interface PhoneNumberHolder {


    short getCountryCode();

    String getPhoneNumber();



    default boolean isValid(){
        if (getPhoneNumber().length() < 6){
            return false;
        }
        return true; // TODO:
    }
}
