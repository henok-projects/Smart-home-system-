package com.galsie.gcs.microservicecommon.lib.gcsawareness;

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceGeneralLocalProperties;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Slf4j
public class GCSMicroserviceAwarenessRabbitMQPublisher {


    @Autowired
    GCSMicroserviceGeneralLocalProperties gcsMicroserviceGeneralLocalProperties;

    @Autowired
    GCSMicroserviceAwarenessService gcsMicroserviceAwarenessService;

    public static final int AWARENESS_POLLING_INTERVAL = 5;

    @Autowired
    @Lazy
    GCSRabbitTemplate rabbitTemplate;

    

    @Scheduled(fixedRate = 1000 * 60 * 60* 24* (AWARENESS_POLLING_INTERVAL-1))
    public void makeAwarenessBroadcast() throws InterruptedException {
        System.out.println("makeAwarenessBroadcast");
        var dateTime = LocalDateTime.now();
        var gcsMicroserviceOpt = gcsMicroserviceGeneralLocalProperties.getGCSMicroservice();
        if(gcsMicroserviceOpt.isEmpty()){
            log.info("No valid GCSMicroservice found in GCSMicroserviceGeneralLocalProperties");
            return;
        }
        var awarenessBroadcastDTO = GCSMicroserviceAwarenessStatusDTO.builder()
                .serviceName(gcsMicroserviceOpt.get())
                .version(gcsMicroserviceGeneralLocalProperties.getVersion())
                .instanceId(gcsMicroserviceGeneralLocalProperties.getInstanceUniqueId())
                .lastPollTime(dateTime)
                .nextPollTime(dateTime.plusDays(AWARENESS_POLLING_INTERVAL))
                .build();
        gcsMicroserviceAwarenessService.updateThisMicroserviceAwarenessStatus(awarenessBroadcastDTO);
    }

}
