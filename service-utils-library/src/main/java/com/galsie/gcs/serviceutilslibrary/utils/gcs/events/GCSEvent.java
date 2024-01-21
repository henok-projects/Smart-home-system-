package com.galsie.gcs.serviceutilslibrary.utils.gcs.events;

/**
 * An GCSEvent is called by an {@link GCSEventManager} and is received by @OnGCSEvent annotated methods in an {@link GCSEventListener}
 */
public interface GCSEvent {


    /**
     * IF an event isCancellable, it can be cancelled by called {@link GCSEvent#setCancelled(boolean)}
     * @return True if the event is Cancellable, false otherwise
     */
    boolean isCancellable();

    /**
     * An GCSEvent should be called before some transaction is done.
     * - If an GCSEvent is cancelled, the transaction is rolled-back and changes are reverted.
     * @return True if the event is cancelled, false otherwise
     */
    boolean isCancelled();

    /**
     * Cancels the event
     * @param isCancelled True of the event should be cancelled, false otherwise
     */
    void setCancelled(boolean isCancelled);
}
