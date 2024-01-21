package com.galsie.gcs.testservice.listeners.gcsevents;

import com.galsie.gcs.microservicecommon.lib.gcsevents.GCSGlobalEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.OnGCSEvent;
import com.galsie.gcs.testservice.events.TestGCSEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@GCSGlobalEventListener
public class TestGCSListener implements GCSEventListener {
    static final Logger logger = LogManager.getLogger();

    @OnGCSEvent
    public void onTestGCSEvent(TestGCSEvent event){
        logger.info("RECEIVED TEST GCS EVENT WITH NAME: " + event.getName());
    }

}
