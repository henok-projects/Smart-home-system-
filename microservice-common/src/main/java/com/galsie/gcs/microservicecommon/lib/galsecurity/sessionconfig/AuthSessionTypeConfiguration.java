package com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig;

import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.GalSecurityAuthenticator;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class define the configuration for a given {@link GalSecurityAuthSessionType}, holding the authenticator(remote or local)
 * a boolean flag to if auto-configuration is set up and a {@link ManualAuthSessionTypeConfiguration} object that defines
 * a list of manually specified paths that are to be authenticated
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AuthSessionTypeConfiguration {

    @NotNull
    private GalSecurityAuthenticator authenticator;

    @NotNull
    private boolean enableAutoConfiguration;

    @Nullable
    private ManualAuthSessionTypeConfiguration manualConfiguration;

}
