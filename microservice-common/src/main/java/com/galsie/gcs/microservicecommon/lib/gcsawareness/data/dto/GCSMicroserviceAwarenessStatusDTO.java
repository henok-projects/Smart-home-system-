package com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto;

import com.galsie.gcs.microservicecommon.lib.gcsawareness.GCSMicroserviceAwarenessRabbitMQPublisher;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This class is used by a microservice to inform other microservices that are up and functioning at certain intervals. this DTO is then broadcast over RabbitMQ uia {@link com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType#GCS_MICROSERVICE_AWARENESS_NOTIFICATIONS}
 * it is used primarily in {@link GCSMicroserviceAwarenessRabbitMQPublisher}
 */
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GCSMicroserviceAwarenessStatusDTO {

    @NotNull
    private GCSMicroservice serviceName;

    @NotNull
    private String version;

    @NotNull
    private String instanceId;
    
    @NotNull
    private LocalDateTime lastPollTime;
    
    @NotNull
    private LocalDateTime nextPollTime;


    public void updateTime(Integer intervalDays){
        this.lastPollTime = LocalDateTime.now();
        this.nextPollTime = this.lastPollTime.plusDays(intervalDays);
    }


}
