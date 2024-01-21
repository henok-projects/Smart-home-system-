package com.galsie.gcs.microservicecommon.lib.gcsawareness.events;

import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SomeMicroserviceAwarenessExtendedEvent extends SomeMicroserviceAwarenessEvent {

    public SomeMicroserviceAwarenessExtendedEvent(GCSMicroserviceAwarenessStatusDTO gcsMicroserviceAwarenessStatusDTO, LocalDateTime eventTime) {
        super(gcsMicroserviceAwarenessStatusDTO, eventTime);
    }

}
