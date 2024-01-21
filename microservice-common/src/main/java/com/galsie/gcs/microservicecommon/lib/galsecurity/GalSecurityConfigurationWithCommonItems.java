package com.galsie.gcs.microservicecommon.lib.galsecurity;

import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.GalSecurityAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.apiclient.RemoteGCSAPIClientAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.galdevice.RemoteGalDeviceAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.microservice.RemoteGCSMicroserviceAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.RemoteUserAuthenticatorService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig.AuthSessionTypeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper class that makes using {@link GalSecurityConfiguration} more convenient through implementing helper functions which provide common configuration items
 */
public abstract class GalSecurityConfigurationWithCommonItems extends GalSecurityConfiguration{

    /**
     * Autowire all remote authenticators so that we can conveniently setup remote authentication for a session as part of a {@link GalSecurityRequestsFilter}
     */
    @Autowired
    RemoteGCSMicroserviceAuthenticatorService remoteGCSMicroserviceAuthenticatorService;

    @Autowired
    RemoteGCSAPIClientAuthenticatorService remoteGCSAPIClientAuthenticatorService;

    @Autowired
    RemoteUserAuthenticatorService remoteUserAuthService;

    @Autowired
    RemoteGalDeviceAuthenticatorService remoteGalDeviceAuthenticatorService;

//    /**
//     * For each given {@link GalSecurityAuthSessionType}, gets a {@link GalSecurityRequestsFilter} which is setup so that:
//     * - The filtered paths are autoconfigured through methods annotated with {@link AuthenticatedGCSRequest} with this session type
//     * - The {@link GalSecurityAuthenticator} for this session type is used (gotten through {@link GalSecurityConfigurationWithCommonItems#getRemoteAuthenticatorFor(GalSecurityAuthSessionType)})
//     * @param galSecurityAuthSessionTypes The session types for which we need the request filters
//     * @return a list of {@link GalSecurityRequestsFilter} (one for each session type)
//     */
//    protected List<GalSecurityRequestsFilter> getAutoConfiguredAndRemoteAuthenticatedRequestFiltersFor(GalSecurityAuthSessionType... galSecurityAuthSessionTypes){
//        return Arrays.stream(galSecurityAuthSessionTypes)
//                .map((this::getAutoConfiguredRemoteAuthenticatedRequestFilterFor))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Gets a {@link GalSecurityRequestsFilter} for the input session type so that:
//     * - The filtered paths are autoconfigured through methods annotated with {@link AuthenticatedGCSRequest} with this session type
//     * - The {@link GalSecurityAuthenticator} for this session type is used (gotten through {@link GalSecurityConfigurationWithCommonItems#getRemoteAuthenticatorFor(GalSecurityAuthSessionType)})
//     * @param galSecurityAuthSessionType The session type for which we need the request filters
//     * @return
//     */
//    protected GalSecurityRequestsFilter getAutoConfiguredRemoteAuthenticatedRequestFilterFor(GalSecurityAuthSessionType galSecurityAuthSessionType){
//        return new AutoConfiguredGalSecurityRequestsFilter(galSecurityAuthSessionType, getRemoteAuthenticatorFor(galSecurityAuthSessionType));
//    }

    /**
     * Gets the remote authenticator service given a session type
     * @param galSecurityAuthSessionType The session type to get the remote authenticator for
     * @return The {@link GalSecurityAuthenticator} setup to do remote authentication for this session type
     */
    protected GalSecurityAuthenticator getRemoteAuthenticatorFor(GalSecurityAuthSessionType galSecurityAuthSessionType){
        return switch(galSecurityAuthSessionType){
            case GCS_MICROSERVICE -> remoteGCSMicroserviceAuthenticatorService;
            case GCS_API_CLIENT -> remoteGCSAPIClientAuthenticatorService;
            case USER -> remoteUserAuthService;
            case GALDEVICE -> remoteGalDeviceAuthenticatorService;
            case TEST -> null; // TODO: REMOVE TEST
        };
    }

    /**
     * Gets a map of {@link GalSecurityAuthSessionType} to {@link AuthSessionTypeConfiguration} for each given
     * {@link GalSecurityAuthSessionType}. the {@link AuthSessionTypeConfiguration} is set up with a remote authenticator for the session type,
     * sets the {@link AuthSessionTypeConfiguration#getManualConfiguration} to true and {@link AuthSessionTypeConfiguration#getManualConfiguration} to null
     * @param galSecurityAuthSessionTypes an array of {@link GalSecurityAuthSessionType} for which we need the {@link AuthSessionTypeConfiguration} for
     * @return
     */
    protected Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> getRemoteAuthSessionTypeConfigurationMapFor(GalSecurityAuthSessionType... galSecurityAuthSessionTypes){
        return Arrays.stream(galSecurityAuthSessionTypes)
                .collect(Collectors.toMap(
                        galSecurityAuthSessionType -> galSecurityAuthSessionType,
                        galSecurityAuthSessionType -> new AuthSessionTypeConfiguration(getRemoteAuthenticatorFor(galSecurityAuthSessionType), true, null)
                ));
    }

}
