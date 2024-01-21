package com.galsie.gcs.users.configuration.security;

import com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig.AuthSessionTypeConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfigurationWithCommonItems;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.users.service.authentication.LocalUserAuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The UsersGalSecurityConfiguration configures Galsie's security for users.
 * - It extends {@link GalSecurityConfigurationWithCommonItems} which implements functionalities for applying the request filters
 * - These request filters are defined in {@link UsersGalSecurityConfiguration#getRequestFilters()} and are:
 *   - GCSMicroservice and APIClient remote authenticators filters
 *   - A local user authentication filter which uses {@link LocalUserAuthenticatorService}
 */
@Configuration
public class UsersGalSecurityConfiguration extends GalSecurityConfigurationWithCommonItems {

    @Autowired
    LocalUserAuthenticatorService authenticator;

//    @Override
//    public List<GalSecurityRequestsFilter> getRequestFilters() {
//        var requestFilters = new ArrayList<GalSecurityRequestsFilter>();
//
//        var remoteAuthenticatedSessionFilters = getAutoConfiguredAndRemoteAuthenticatedRequestFiltersFor(GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT);
//        requestFilters.addAll(remoteAuthenticatedSessionFilters);
//
//        var userRequestsFilter = new AutoConfiguredGalSecurityRequestsFilter(GalSecurityAuthSessionType.USER, authenticator); // filters by paths defined in @AuthenticatedGCSRequest(sessionType = GalSecurityAuthSessionType.USER)
//        requestFilters.add(userRequestsFilter);
//        // can add as many filters as needed, for as many purposes as needed.
//        return requestFilters;
//    }

    @Override
    public List<GalSecurityAuthSessionType> getCacheEnabledSessions() {
        return Arrays.asList(GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT, GalSecurityAuthSessionType.GALDEVICE);
    }

    @Override
    public Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> getSessionTypeConfigurations() {
       var map = this.getRemoteAuthSessionTypeConfigurationMapFor(GalSecurityAuthSessionType.GCS_MICROSERVICE,
               GalSecurityAuthSessionType.GCS_API_CLIENT);
       map.put(GalSecurityAuthSessionType.USER, new AuthSessionTypeConfiguration(authenticator,true, null));
       return map;
    }

}
