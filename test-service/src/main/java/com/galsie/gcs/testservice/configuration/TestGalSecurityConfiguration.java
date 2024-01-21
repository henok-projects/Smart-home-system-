package com.galsie.gcs.testservice.configuration;

import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.GalSecurityRequestsFilter;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class TestGalSecurityConfiguration extends GalSecurityConfigurationWithCommonItems {
    @Override
    public List<GalSecurityRequestsFilter> getRequestFilters() {
        return Arrays.asList();
    }

    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return Arrays.asList();
    }
}
