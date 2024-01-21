package com.galsie.gcs.serviceutilslibrary.utils.gcs.events;

import com.galsie.lib.utils.ReflectionUtils;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A RegisteredGCSEventListener is an {@link GCSEventListener} that has been registered with an {@link GCSEventManager}. The registered
 * event listener holds:
 * - The {@link GCSEventListener}
 * - A map of Methods to be called for an EventType
 *
 * A static method {@link RegisteredGCSEventListener#newInstanceForListener(GCSEventListener)} to load a new instance of the RegisteredGCSEventListener
 *
 */
@Getter
public class RegisteredGCSEventListener {

    GCSEventListener listener;
    HashMap<Class<? extends GCSEvent>, List<Method>> eventToMethodsMap;

    private RegisteredGCSEventListener(GCSEventListener listener, HashMap<Class<? extends GCSEvent>, List<Method>> eventToMethodsMap){
        this.listener = listener;
        this.eventToMethodsMap = eventToMethodsMap;
    }

    /**
     * Receives an GCSEvent.
     * - Gets all methods associated with that event {@link RegisteredGCSEventListener#eventToMethodsMap}
     * - Invokes those methods on the listener and the event
     * @param event The {@link GCSEvent} to receive
     * @param <T> The type of GCSEvent
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws IllegalAccessException if this Method object is enforcing Java language access control and the underlying method is inaccessible.
     */
    public <T extends GCSEvent> void receiveEvent(T event) throws InvocationTargetException, IllegalAccessException {
        var eventType = event.getClass();
        if (!eventToMethodsMap.containsKey(event.getClass())){
            return;
        }
        for (Method method: eventToMethodsMap.get(event.getClass())){
            method.invoke(this.listener, event);
        }
    }

    /**
     * Gets a new {@link RegisteredGCSEventListener} instance from an {@link GCSEventListener}. Does so by loading all the methods that:
     * - Are annotated with @OnGCSEvent
     * - Have one parameter which is a subclass of event
     * It then stores these methods in a structure which maps an event type to a list of methods to be called
     * - When {@link RegisteredGCSEventListener#receiveEvent(GCSEvent)} is called, the list of methods associated with that event type are called on that event instance.
     * @param GCSEventListener The GCSEvent Listener to be registered
     * @return a new {@link RegisteredGCSEventListener} instance
     */
    public static RegisteredGCSEventListener newInstanceForListener(GCSEventListener gcsEventListener){
        HashMap<Class<? extends GCSEvent>, List<Method>> eventToMethodsMap = new HashMap<>();
        var annotatedMethods =  ReflectionUtils.getAnnotatedMethods(gcsEventListener.getClass(), OnGCSEvent.class, GCSEvent.class);
        for (Method method: annotatedMethods){
            var eventType = (Class<? extends GCSEvent>) method.getParameters()[0].getType();
            if (!eventToMethodsMap.containsKey(eventType)){
                eventToMethodsMap.put(eventType, new ArrayList<>());
            }
            eventToMethodsMap.get(eventType).add(method);
        }
        return new RegisteredGCSEventListener(gcsEventListener, eventToMethodsMap);
    }


}
