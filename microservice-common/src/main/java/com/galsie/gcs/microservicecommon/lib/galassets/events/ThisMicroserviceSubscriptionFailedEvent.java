package com.galsie.gcs.microservicecommon.lib.galassets.events;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;

public class ThisMicroserviceSubscriptionFailedEvent extends GCSEventCommonImpl {
    @Override
    public boolean isCancellable() {
        return false;
    }
}
