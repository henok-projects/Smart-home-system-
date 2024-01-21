package com.galsie.gcs.microservicecommon.lib.gcsawareness.events;

import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public abstract class SomeMicroserviceAwarenessEvent extends GCSEventCommonImpl {

    private GCSMicroserviceAwarenessStatusDTO gcsMicroserviceAwarenessStatusDTO;

    private LocalDateTime eventTime ;

    @Override
    public boolean isCancellable() {
        return false;
    }

}
