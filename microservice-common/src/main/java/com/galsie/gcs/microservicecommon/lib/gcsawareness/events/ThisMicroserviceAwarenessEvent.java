package com.galsie.gcs.microservicecommon.lib.gcsawareness.events;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;

public class ThisMicroserviceAwarenessEvent extends GCSEventCommonImpl {

    @Override
    public boolean isCancellable() {
        return false;
    }

}
