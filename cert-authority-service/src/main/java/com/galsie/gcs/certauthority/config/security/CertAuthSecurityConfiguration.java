package com.galsie.gcs.certauthority.config.security;

import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig.AuthSessionTypeConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Certificate Authority Security configuration
 *
 * {@inheritDoc}
 * Inherits {@link GalSecurityConfiguration} to comply with the micro-service-common security architecture
 */
@Configuration
public class CertAuthSecurityConfiguration extends GalSecurityConfigurationWithCommonItems {

    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return Arrays.asList(GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT, GalSecurityAuthSessionType.USER, GalSecurityAuthSessionType.GALDEVICE);
    }

    @Override
    public Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> getSessionTypeConfigurations() {
        return getRemoteAuthSessionTypeConfigurationMapFor(GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT, GalSecurityAuthSessionType.USER, GalSecurityAuthSessionType.GALDEVICE);
    }
}
