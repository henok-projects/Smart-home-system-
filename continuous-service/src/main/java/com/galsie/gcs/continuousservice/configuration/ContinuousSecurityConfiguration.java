package com.galsie.gcs.continuousservice.configuration;

import com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig.AuthSessionTypeConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
public class ContinuousSecurityConfiguration extends GalSecurityConfigurationWithCommonItems {


    @Autowired
    ContinuousSocketsLocalProperties continuousSocketsLocalProperties;


    @Override
    public HttpSecurity setupHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // disable basic http for sockets
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
    }


    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return Arrays.asList(GalSecurityAuthSessionType.USER, GalSecurityAuthSessionType.GCS_API_CLIENT, GalSecurityAuthSessionType.GALDEVICE, GalSecurityAuthSessionType.GCS_MICROSERVICE);
    }

    @Override
    public Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> getSessionTypeConfigurations() {
        return getRemoteAuthSessionTypeConfigurationMapFor(GalSecurityAuthSessionType.USER, GalSecurityAuthSessionType.GCS_API_CLIENT, GalSecurityAuthSessionType.GCS_MICROSERVICE);
    }

}
