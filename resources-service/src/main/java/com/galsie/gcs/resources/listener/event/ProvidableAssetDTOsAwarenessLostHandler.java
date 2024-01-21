package com.galsie.gcs.resources.listener.event;

import com.galsie.gcs.microservicecommon.lib.gcsawareness.GCSMicroserviceAwarenessService;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.events.SomeMicroserviceAwarenessLostEvent;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.resources.service.providableassetdtos.MicroserviceProvidableAssetDTOsSubscriptionService;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.OnGCSEvent;
import com.galsie.lib.utils.pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProvidableAssetDTOsAwarenessLostHandler implements GCSEventListener {

    @Autowired
    GCSMicroserviceAwarenessService gcsMicroserviceAwarenessService;

    @Autowired
    MicroserviceProvidableAssetDTOsSubscriptionService microserviceProvidableAssetDTOsSubscriptionService;


    @OnGCSEvent
    public void handlePoolingEvent(SomeMicroserviceAwarenessLostEvent event) {
        List<Pair<GCSMicroservice, String>> pairs = event.getLostMicroservicesAwarenessStatusDTOs().stream().map(e -> new Pair<>(e.getServiceName(), e.getInstanceId())).toList();
       microserviceProvidableAssetDTOsSubscriptionService.deleteAllByMicroserviceAndUniqueInstanceIdInPairs(pairs);
    }

}
