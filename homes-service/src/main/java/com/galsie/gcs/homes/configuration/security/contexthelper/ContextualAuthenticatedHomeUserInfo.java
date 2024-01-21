package com.galsie.gcs.homes.configuration.security.contexthelper;

import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.UserSecurityAuthSession;
import com.galsie.lib.utils.lang.NotNull;
import com.galsie.lib.utils.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: use updated system
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ContextualAuthenticatedHomeUserInfo {

    @NotNull
    UserSecurityAuthSession userSecurityAuthSession;

    @Nullable
    HomeEntity homeEntity;

    @Nullable
    HomeUserEntity homeUserEntity;

    @Nullable
    HomeResponseErrorType homeResponseError;

    public boolean hasError(){
        return this.homeResponseError != null;
    }

    public static ContextualAuthenticatedHomeUserInfo error(@NotNull UserSecurityAuthSession authSession, @Nullable HomeEntity homeEntity, @Nullable HomeUserEntity userEntity, HomeResponseErrorType errorType){
        return new ContextualAuthenticatedHomeUserInfo(authSession, null, null, errorType);
    }

    public static ContextualAuthenticatedHomeUserInfo success(@NotNull UserSecurityAuthSession securityAuthSession, @com.galsie.lib.utils.lang.NotNull HomeEntity homeEntity, @NotNull HomeUserEntity user){
        return new ContextualAuthenticatedHomeUserInfo(securityAuthSession, homeEntity, user, null);
    }


}
