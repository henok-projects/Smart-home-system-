package com.galsie.gcs.microservicecommon.lib.gcsawareness;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.assetsubscription.GCSSubscriptionState;
import com.galsie.gcs.microservicecommon.lib.galassets.init.*;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.discrete.GCSMicroserviceAwarenessState;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.events.*;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManager;
import com.galsie.lib.utils.pair.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GCSMicroserviceAwarenessService {

    private GCSMicroserviceAwarenessState gcsMicroserviceAwarenessState;

    @Autowired
    @Lazy
    GCSEventManager gcsEventManager;

    @Autowired
    @Lazy
    GCSRabbitTemplate rabbitTemplate;

    private final GCSMicroserviceAwarenessCache gcsMicroserviceAwarenessCache;

    private final Integer AWARENESSPOLLINGINTERVALDAYS = 5;
    public GCSMicroserviceAwarenessService() {
        this.gcsMicroserviceAwarenessCache = new GCSMicroserviceAwarenessCache();
    }

    public void updateThisMicroserviceAwarenessStatus(GCSMicroserviceAwarenessStatusDTO gcsMicroserviceAwarenessStatusDTO) throws InterruptedException {
        var sent = false;
        long totalTimeSpentRetrying = 0;
        long nextRetyingInterval = 5 * 1000; //5 seconds
        processCurrentAwarenessDTOs();
        while(!sent && totalTimeSpentRetrying < 3.7 *24 * 60 * 60 * 1000){ //3.7 days
            try {
                gcsMicroserviceAwarenessStatusDTO.updateTime(AWARENESSPOLLINGINTERVALDAYS);
                rabbitTemplate.convertAndSend(GCSRabbitMQCommonQueueType.GCS_MICROSERVICE_AWARENESS_NOTIFICATIONS,gcsMicroserviceAwarenessStatusDTO);
                sent = true;
                log.info("Sent awareness status for "+ gcsMicroserviceAwarenessStatusDTO.getServiceName()  +" over RabbitMQ");
            }catch (Exception e){
                log.warn("Failed to send awareness status over RabbitMQ ", e);
                log.warn("Putting thread to sleep for "+ nextRetyingInterval +" milliseconds before retrying");
                Thread.sleep(nextRetyingInterval);
                totalTimeSpentRetrying += nextRetyingInterval;
                log.info( "Total time spent retrying: "+ totalTimeSpentRetrying +" milliseconds");
                nextRetyingInterval *= 2;
            }
        }
        if(!sent){
            log.warn("Failed to send awareness status over RabbitMQ");
            gcsEventManager.callEvent(new ThisMicroserviceAwarenessLostEvent());
            if(GCSMicroserviceAwarenessState.AWARENESS_GAINED.equals(gcsMicroserviceAwarenessState)){
                gcsMicroserviceAwarenessState = GCSMicroserviceAwarenessState.AWARENESS_LOST;
                ProvidableAssetDTOsStartupAndSubscriptionService.setSubscriptionState(GCSSubscriptionState.NOT_SUBSCRIBED);
            }
            return;
        }
        if(GCSMicroserviceAwarenessState.STARTING_UP.equals(gcsMicroserviceAwarenessState)){
            gcsEventManager.callEvent(new ThisMicroserviceAwarenessGainedEvent());
            gcsMicroserviceAwarenessState = GCSMicroserviceAwarenessState.AWARENESS_GAINED;
            return;
        }
        if(GCSMicroserviceAwarenessState.AWARENESS_LOST.equals(gcsMicroserviceAwarenessState)){
            gcsEventManager.callEvent(new ThisMicroserviceAwarenessRegainedEvent());
            gcsMicroserviceAwarenessState = GCSMicroserviceAwarenessState.AWARENESS_GAINED;
            return;
        }
        gcsEventManager.callEvent(new ThisMicroserviceAwarenessExtendedEvent());
    }

    public void updateSomeMicroserviceAwarenessStatus(GCSMicroserviceAwarenessStatusDTO gcsMicroserviceAwarenessStatusDTO) {
        var pair = new Pair<>(gcsMicroserviceAwarenessStatusDTO.getServiceName(), gcsMicroserviceAwarenessStatusDTO.getInstanceId());
        var previousAwarenessStatus = gcsMicroserviceAwarenessCache.getMicroserviceAwarenessStatus(pair);
        gcsMicroserviceAwarenessCache.updateMicroserviceAwarenessStatus(pair, gcsMicroserviceAwarenessStatusDTO);
        if(previousAwarenessStatus == null){
            gcsEventManager.callEvent(new SomeMicroserviceAwarenessGainedEvent(gcsMicroserviceAwarenessStatusDTO, LocalDateTime.now()));
        }else{
            gcsEventManager.callEvent(new SomeMicroserviceAwarenessExtendedEvent(gcsMicroserviceAwarenessStatusDTO, LocalDateTime.now()));
        }
        processCurrentAwarenessDTOs();
    }

    public List<GCSMicroserviceAwarenessStatusDTO> getAllMicroserviceInstanceAwarenessStatus(GCSMicroservice gcsMicroservice) {
        return gcsMicroserviceAwarenessCache.getAllMicroserviceInstanceAwareStatus(gcsMicroservice);
    }

    public Optional<GCSMicroserviceAwarenessStatusDTO> getMicroserviceAwarenessStatus(GCSMicroservice gcsMicroservice, String instanceId) {
        return Optional.ofNullable(gcsMicroserviceAwarenessCache.getMicroserviceAwarenessStatus(new Pair<>(gcsMicroservice, instanceId)));
    }

    private void processCurrentAwarenessDTOs(){
        var currentAwarenessDTOs = gcsMicroserviceAwarenessCache.getAllMicroserviceAwarenessStatus();
        var previousAwarenessDTOs = new ArrayList<GCSMicroserviceAwarenessStatusDTO>();
        for(var awarenessDTO : currentAwarenessDTOs){
            if(awarenessDTO.getNextPollTime().isBefore(LocalDateTime.now())){
                previousAwarenessDTOs.add(awarenessDTO);
            }
        }
        if(!previousAwarenessDTOs.isEmpty()){
            gcsEventManager.callEvent(new SomeMicroserviceAwarenessLostEvent(null, null, previousAwarenessDTOs));
        }
    }
}

