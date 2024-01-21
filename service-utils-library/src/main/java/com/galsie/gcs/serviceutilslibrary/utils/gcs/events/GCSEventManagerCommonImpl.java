package com.galsie.gcs.serviceutilslibrary.utils.gcs.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Common Implementation for {@link GCSEventManager}. Has methods to
 * - Call Events
 * - Register Listeners. Stores registered listeners in a {@link RegisteredGCSEventListener}
 */

public class GCSEventManagerCommonImpl implements GCSEventManager {

    static Logger logger = LogManager.getLogger();
    private final List<RegisteredGCSEventListener> registeredGCSEventListeners = new ArrayList<>();

    @Override
    public <T extends GCSEvent> T callEvent(T event) {
        for (RegisteredGCSEventListener registeredGCSEventListener : this.registeredGCSEventListeners) {
            try {
                registeredGCSEventListener.receiveEvent(event);
            } catch (Exception ex) {
                logger.error("The listener " + registeredGCSEventListener.getClass() + "Failed to receive the event " + event.getClass());
                ex.printStackTrace();
            }
        }
        return event;
    }

    @Override
    public void registerListener(GCSEventListener listener) {
        registeredGCSEventListeners.add(RegisteredGCSEventListener.newInstanceForListener(listener));
    }

    @Override
    public void registerListeners(Collection<? extends GCSEventListener> eventListenerList) {
        registeredGCSEventListeners.addAll(eventListenerList.stream().map(RegisteredGCSEventListener::newInstanceForListener).toList());
    }
}
