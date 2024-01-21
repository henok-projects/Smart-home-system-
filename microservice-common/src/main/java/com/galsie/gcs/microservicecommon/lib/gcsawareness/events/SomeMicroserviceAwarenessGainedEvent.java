package com.galsie.gcs.microservicecommon.lib.gcsawareness.events;

import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SomeMicroserviceAwarenessGainedEvent extends SomeMicroserviceAwarenessEvent{


    public SomeMicroserviceAwarenessGainedEvent(GCSMicroserviceAwarenessStatusDTO gcsMicroserviceAwarenessStatusDTO, LocalDateTime now) {
        super(gcsMicroserviceAwarenessStatusDTO, now);
    }

}
