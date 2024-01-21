package com.galsie.gcs.microservicecommon.lib.gcsevents;

import com.galsie.gcs.microservicecommon.lib.gcsevents.init.GCSGlobalEventListenerAutoConfig;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManager;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManagerCommonImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public abstract class GCSEventsConfiguration {
    /**
     * Global GCSEvent manager is used for autocnifgured {@link GCSGlobalEventListener}s
     */
    @Bean
    @Primary
    public GCSEventManager getGlobalEventManager(){
        GCSEventManagerCommonImpl globalEventManager = new GCSEventManagerCommonImpl();
        return globalEventManager;
    }

    @Bean
    public GCSGlobalEventListenerAutoConfig gcsGlobalEventListenerRegistrar(){
        return new GCSGlobalEventListenerAutoConfig();
    }

}
