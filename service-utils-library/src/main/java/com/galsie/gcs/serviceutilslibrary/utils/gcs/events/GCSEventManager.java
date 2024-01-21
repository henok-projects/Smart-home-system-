package com.galsie.gcs.serviceutilslibrary.utils.gcs.events;

import java.util.Collection;
import java.util.List;

/**
 * An GCSEventManager has a list of registered {@link GCSEventListener}
 * - It has methods to call an event, which causes all registered listeners to receive the event
 */
public interface GCSEventManager {
    <T extends GCSEvent> T callEvent(T e);

    void registerListener(GCSEventListener listener);

    void registerListeners(Collection<? extends GCSEventListener> eventListenerList);
}
