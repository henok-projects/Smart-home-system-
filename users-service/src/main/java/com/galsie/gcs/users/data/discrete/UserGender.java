package com.galsie.gcs.users.data.discrete;

import java.util.Optional;

public enum UserGender {

    MALE, FEMALE, NOT_SPECIFIED, OTHER;

    public static Optional<UserGender> fromString(String name){
        for(UserGender gender: UserGender.values()){
            if(gender.name().toLowerCase().equals(name)){
                return Optional.of(gender);
            }
        }
        return Optional.empty();
    }
}
