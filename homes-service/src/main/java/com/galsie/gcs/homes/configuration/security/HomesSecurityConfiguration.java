package com.galsie.gcs.homes.configuration.security;

import com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig.AuthSessionTypeConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
public class HomesSecurityConfiguration extends GalSecurityConfigurationWithCommonItems {

    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions(){
        return Arrays.asList(GalSecurityAuthSessionType.values()); // cache for all
    }

    @Override
    public Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> getSessionTypeConfigurations() {
        return this.getRemoteAuthSessionTypeConfigurationMapFor(GalSecurityAuthSessionType.implementedAuthenticators());
    }

}
