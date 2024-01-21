package com.galsie.gcs.serviceutilslibrary.utils.gcs.events;

public abstract class GCSEventCommonImpl implements GCSEvent {
    private boolean isCancelled = false;

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}
