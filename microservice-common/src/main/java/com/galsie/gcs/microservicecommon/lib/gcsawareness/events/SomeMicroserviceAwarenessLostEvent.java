package com.galsie.gcs.microservicecommon.lib.gcsawareness.events;

import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import com.sun.istack.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SomeMicroserviceAwarenessLostEvent extends SomeMicroserviceAwarenessEvent {

    @NotNull
    private final List<GCSMicroserviceAwarenessStatusDTO> lostMicroservicesAwarenessStatusDTOs;

    public SomeMicroserviceAwarenessLostEvent(GCSMicroserviceAwarenessStatusDTO gcsMicroserviceAwarenessStatusDTO, LocalDateTime now, List<GCSMicroserviceAwarenessStatusDTO> lostMicroservicesAwarenessStatusDTOs) {
        super(gcsMicroserviceAwarenessStatusDTO, now);
        this.lostMicroservicesAwarenessStatusDTOs = lostMicroservicesAwarenessStatusDTOs;
    }

}
