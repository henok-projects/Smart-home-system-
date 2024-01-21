package com.galsie.gcs.resources.listener.event;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.update.GCSUpdatedProvidedAssetDTOTypes;
import com.galsie.gcs.resources.event.SingleLoadedAssetFileBootStrapDoneEvent;
import com.galsie.gcs.resources.event.SingleLoadedAssetGroupBootstrapDoneEvent;
import com.galsie.gcs.resources.service.providableassetdtos.MicroserviceProvidableAssetDTOsSubscriptionService;
import com.galsie.gcs.resources.service.providableassetdtos.ProvidableAssetDTOsProvider;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.OnGCSEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProvidableAssetDTOHandler implements GCSEventListener {

    @Autowired
    ProvidableAssetDTOsProvider providableAssetDTOsProvider;

    @Autowired
    MicroserviceProvidableAssetDTOsSubscriptionService subscriptionService;

    @OnGCSEvent
    public void handleSingleLoadedSingleAssetFileBootstrapDoneEvent(SingleLoadedAssetFileBootStrapDoneEvent event) {
        var providableAssetDTOTypeOpt = ProvidableAssetDTOType.getAssetDTOTypeFromPath(event.getPath());
        if(providableAssetDTOTypeOpt.isEmpty()){
            return;
        }
        providableAssetDTOsProvider.loadProvidedAssetDTO(providableAssetDTOTypeOpt.get(), event.isVersionChanged());
    }

    @OnGCSEvent
    public void handleLoadedAssetGroupBootstrapDoneEvent(SingleLoadedAssetGroupBootstrapDoneEvent event) {
        if(!event.isAnyFileVersionChanged()){
            return;
        }
        var updatedProvidedAssetDTOTypes = providableAssetDTOsProvider.getVersionChangedDTOTypes();
        subscriptionService.broadcastChangedProvidableAssetDTOs(GCSUpdatedProvidedAssetDTOTypes.builder().updatedProvidedAssetDTOTypes(updatedProvidedAssetDTOTypes).build());
        providableAssetDTOsProvider.clearVersionChangedProvidedAssetDTOTypes();

    }


}
