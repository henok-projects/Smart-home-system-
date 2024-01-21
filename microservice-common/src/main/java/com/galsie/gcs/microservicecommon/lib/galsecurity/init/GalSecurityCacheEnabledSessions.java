package com.galsie.gcs.microservicecommon.lib.galsecurity.init;

import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Holds the list of {@link GalSecurityAuthSessionType} for which caching is enabled for the convenience of using these
 *
 *
 * NOTE: Bean is setup in {@link GalSecurityConfiguration}
 *
 * Note: Bean is used by {@link com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQConfigurationWithCommonItems}
 */
@AllArgsConstructor
@Getter
public class GalSecurityCacheEnabledSessions {
    List<GalSecurityAuthSessionType> cacheEnabledSessionTypes;
}