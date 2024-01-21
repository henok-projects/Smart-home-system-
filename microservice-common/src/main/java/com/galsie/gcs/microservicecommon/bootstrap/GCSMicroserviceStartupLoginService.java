package com.galsie.gcs.microservicecommon.bootstrap;

import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.events.ThisMicroserviceLoggedInEvent;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.login.GCSMicroserviceSentryLoginService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.events.ThisMicroserviceLogInFailedEvent;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GCSMicroserviceStartupLoginService {

    //@Qualifier("GCSSentrySecurityConfiguration")
    @Autowired(required = false)
    GalSecurityConfiguration galSecurityConfiguration;

    @Autowired
    GCSMicroserviceSentryLoginService gcsMicroserviceSentryLoginService;

    @Autowired
    GCSEventManager eventManager;

    public void login() throws Exception {
        if (galSecurityConfiguration == null){
            return;
        }
        if (!galSecurityConfiguration.isMicroserviceAutoLoginWithSentryEnabled()){
            return;
        }
        log.info("GCS Microservice Security: Auto-login with GCS-SENTRY is enabled, logging in...");
        try{
            gcsMicroserviceSentryLoginService.login();
        } catch (Exception e) { //the errors of this exception are logged in GCSMicroserviceSentryLoginService
            eventManager.callEvent(new ThisMicroserviceLogInFailedEvent());
        }
        log.info("GCS Microservice Security: LOGIN SUCCESS!");
        eventManager.callEvent(new ThisMicroserviceLoggedInEvent());
    }

}
