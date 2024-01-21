package com.galsie.gcs.testservice.events;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;
import lombok.Getter;

@Getter
public class TestGCSEvent extends GCSEventCommonImpl {
    private String name;
    public TestGCSEvent(String name){
        this.name = name;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }
}
