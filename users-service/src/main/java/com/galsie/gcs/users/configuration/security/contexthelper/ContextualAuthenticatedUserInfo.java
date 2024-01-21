package com.galsie.gcs.users.configuration.security.contexthelper;

import com.galsie.gcs.users.data.entity.security.UserAuthSessionEntity;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContextualAuthenticatedUserInfo {
    @NotNull
    private UserAuthSessionEntity userAuthSessionEntity;

}