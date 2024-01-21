package com.galsie.gcs.resources.event;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SingleLoadedAssetGroupBootstrapDoneEvent extends GCSEventCommonImpl {

    private AssetGroupType assetGroupType;

    boolean anyFileVersionChanged;

    @Override
    public boolean isCancellable() {
        return false;
    }
}
