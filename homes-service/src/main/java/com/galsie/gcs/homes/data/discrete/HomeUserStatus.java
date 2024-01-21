package com.galsie.gcs.homes.data.discrete;

import java.util.Optional;

public enum HomeUserStatus {
    ACTIVE,
    ACCESS_ENDED,
    LEFT;


    public Optional<HomeResponseErrorType> toHomeResponseErrorType(boolean ignoreUserLeft, boolean ignoreAccessEnded){
        return Optional.ofNullable(auxToHomeResponseErrorType(ignoreUserLeft, ignoreAccessEnded));
    }
    private HomeResponseErrorType auxToHomeResponseErrorType(boolean ignoreUserLeft, boolean ignoreAccessEnded){
        if (this == ACCESS_ENDED && !ignoreAccessEnded){
            return HomeResponseErrorType.ACCESS_ENDED;
        }
        if (this == LEFT && !ignoreUserLeft){
            return HomeResponseErrorType.LEFT_HOME;
        }
        return null;
    }
}
