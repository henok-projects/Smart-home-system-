package com.galsie.gcs.serviceutilslibrary.utils.gcs.events;

/**
 * GCSEventListener must be registered in an {@link GCSEventManager}
 *
 * The {@link GCSEventManager} finds all methods in the GCSEventListener object that:
 * - Are annotated with @onEvent
 * - Have one parameter, which is a subclass of event
 *
 *
 */
public interface GCSEventListener {


    /*
    EXAMPLE METHOD that can be defined in GCSEventListener

    @OnGCSEvent
    public void onSomeEvent(SomeEvent event){
        // do something
    }
     */
}
