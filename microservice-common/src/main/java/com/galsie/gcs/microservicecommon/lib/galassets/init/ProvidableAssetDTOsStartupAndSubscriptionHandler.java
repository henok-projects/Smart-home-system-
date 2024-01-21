package com.galsie.gcs.microservicecommon.lib.galassets.init;

import com.galsie.gcs.microservicecommon.lib.galassets.events.ThisMicroserviceSubscriptionFailedEvent;
import com.galsie.gcs.microservicecommon.lib.gcsevents.GCSGlobalEventListener;
import com.galsie.gcs.microservicecommon.lib.galsecurity.events.ThisMicroserviceLogInFailedEvent;
import com.galsie.gcs.microservicecommon.lib.galsecurity.events.ThisMicroserviceLoggedInEvent;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.OnGCSEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for taking action for successful and unsuccessful login events.
 * in its current implementation its subscribes to DTOs and gets startup DTOs on successful login.
 */
@GCSGlobalEventListener
@Slf4j
public class ProvidableAssetDTOsStartupAndSubscriptionHandler implements GCSEventListener {

    @Autowired(required = false)
    ProvidableAssetDTOsStartupAndSubscriptionService gcsStartupSubscriptionService;

    @OnGCSEvent
    public void onMicroservicesLoggedInEvent(ThisMicroserviceLoggedInEvent event) {
        if(gcsStartupSubscriptionService == null) {
            log.warn("No implementation of " + ProvidableAssetDTOsStartupAndSubscriptionService.class.getName() + " found. Skipping startup DTO subscription");
            return;
        }
        runSubscriptionSteps();
    }

    @OnGCSEvent
    public void onMicroserviceLoginFailedEvent(ThisMicroserviceLogInFailedEvent event) {}

    @OnGCSEvent
    public void onMicroserviceSubscriptionFailedEvent(ThisMicroserviceSubscriptionFailedEvent event){
        var waitUntilInMillis = System.currentTimeMillis()+ (1000 * 60 * 1.5);
        while (System.currentTimeMillis() < waitUntilInMillis){}
        log.info("Retrying asset subscription steps");
        runSubscriptionSteps();
        //performOperationAfterDelay(this::runSubscriptionSteps);
    }

    private void runSubscriptionSteps(){
        gcsStartupSubscriptionService.requestSubscribableToSubscribableDTOs();
        gcsStartupSubscriptionService.requestLoadStartupProvidableAssetDTOs();
    }

    private void performOperationAfterDelay(Runnable operation) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(operation, 5, TimeUnit.MINUTES);
        executor.shutdown();
    }

}
