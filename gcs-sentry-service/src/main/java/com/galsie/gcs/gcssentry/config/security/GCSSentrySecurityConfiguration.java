package com.galsie.gcs.gcssentry.config.security;

import com.galsie.gcs.gcssentry.service.apiclient.LocalGCSAPIClientAuthenticatorService;
import com.galsie.gcs.gcssentry.service.microservice.LocalGCSMicroserviceAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig.AuthSessionTypeConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class GCSSentrySecurityConfiguration extends GalSecurityConfigurationWithCommonItems {
    @Autowired
    LocalGCSMicroserviceAuthenticatorService localGCSMicroserviceAuthenticatorService;

    @Autowired
    LocalGCSAPIClientAuthenticatorService localGCSAPIClientAuthenticatorService;

    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return new ArrayList<>(); // No local caching
    }


    @Override
    public Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> getSessionTypeConfigurations() {
        Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> map = new TreeMap<>();
        map.put(GalSecurityAuthSessionType.GCS_MICROSERVICE, new AuthSessionTypeConfiguration(localGCSMicroserviceAuthenticatorService, true, null));
        map.put(GalSecurityAuthSessionType.GCS_API_CLIENT, new AuthSessionTypeConfiguration(localGCSAPIClientAuthenticatorService, true, null));
        return map;
    }

    @Override
    public boolean isMicroserviceAutoLoginWithSentryEnabled(){
        return false; // this microservice should not login with the sentry
    }

}
