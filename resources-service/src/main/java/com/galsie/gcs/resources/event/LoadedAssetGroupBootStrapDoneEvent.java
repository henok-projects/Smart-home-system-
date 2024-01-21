package com.galsie.gcs.resources.event;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoadedAssetGroupBootStrapDoneEvent extends GCSEventCommonImpl {

    private boolean anyFileVersionChanged;

    @Override
    public boolean isCancellable() {
        return false;
    }

}
