package com.galsie.gcs.microservicecommon.lib.galsecurity.events;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;

public class ThisMicroserviceLogInFailedEvent extends GCSEventCommonImpl {
    @Override
    public boolean isCancellable() {return false;}
}
