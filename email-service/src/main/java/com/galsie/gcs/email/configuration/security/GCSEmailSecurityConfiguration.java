package com.galsie.gcs.email.configuration.security;

import com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig.AuthSessionTypeConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
public class GCSEmailSecurityConfiguration extends GalSecurityConfigurationWithCommonItems {


    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return Arrays.asList(GalSecurityAuthSessionType.GCS_MICROSERVICE);
    }

    @Override
    public Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> getSessionTypeConfigurations() {
        return this.getRemoteAuthSessionTypeConfigurationMapFor(GalSecurityAuthSessionType.GCS_MICROSERVICE);
    }
}
