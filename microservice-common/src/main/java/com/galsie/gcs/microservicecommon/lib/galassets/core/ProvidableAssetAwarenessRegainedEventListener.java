package com.galsie.gcs.microservicecommon.lib.galassets.core;

import com.galsie.gcs.microservicecommon.lib.galassets.init.*;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.events.ThisMicroserviceAwarenessLostEvent;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.events.ThisMicroserviceAwarenessRegainedEvent;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.OnGCSEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class listens to the {@link ThisMicroserviceAwarenessLostEvent} being called
 * and responds by resubscribing the
 */
@Slf4j
public class ProvidableAssetAwarenessRegainedEventListener implements GCSEventListener {

    @Autowired(required = false)
    ProvidableAssetDTOsStartupAndSubscriptionService providableAssetDTOsStartupAndSubscriptionService;

    @OnGCSEvent
    public void onThisMicroserviceAwarenessRegainedEvent(ThisMicroserviceAwarenessRegainedEvent event) {
        try {
            providableAssetDTOsStartupAndSubscriptionService.requestSubscribableToSubscribableDTOs();
            providableAssetDTOsStartupAndSubscriptionService.requestLoadStartupProvidableAssetDTOs();
        } catch (Exception e) {
            log.error("Failed to resubscribe to DTOs", e);
        }
    }

}
