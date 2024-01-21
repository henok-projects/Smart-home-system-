package com.galsie.gcs.microservicecommon.bootstrap;

import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.login.GCSMicroserviceSentryLoginService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Bootstraps the version
 *
 * Logs in with gcs-sentry if login is enabled according to {@link GalSecurityConfiguration#isMicroserviceAutoLoginWithSentryEnabled()}
 */
@Component
public class MicroserviceCommonBootstrapBean implements InitializingBean {

    @Autowired
    MicroServiceVersionCheckerBootstrapService microServiceVersionCheckerBootstrapService;


    @Override
    public void afterPropertiesSet() throws Exception {
        microServiceVersionCheckerBootstrapService.bootstrap();
    }

}
