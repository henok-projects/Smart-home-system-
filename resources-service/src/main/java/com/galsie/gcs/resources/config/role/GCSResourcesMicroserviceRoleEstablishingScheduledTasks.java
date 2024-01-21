package com.galsie.gcs.resources.config.role;

import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GCSResourcesMicroserviceRoleEstablishingScheduledTasks {

    @Autowired
    GCSResourcesMicroserviceRoleEstablishmentService gcsResourcesMicroserviceRoleEstablishmentService;

    @Autowired
    GCSResourcesMicroserviceStateHolder gcsResourcesMicroserviceStateHolder;

    private static final int INTERVAL = 5;

    @Scheduled(fixedDelay = INTERVAL * 60 *1000)
    public void checkEstablishedRoles(){
        if(gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus().equals(GCSResourceMicroserviceStatus.STARTING_UP) || gcsResourcesMicroserviceRoleEstablishmentService.isCheckingEstablishedRoles() || gcsResourcesMicroserviceRoleEstablishmentService.isEstablishingThisAsManager()){
            log.info("An operation is already in progress or this microservice is starting up, skipping role establishment check");
            return;
        }
        gcsResourcesMicroserviceRoleEstablishmentService.checkEstablishedRoles();
    }

}
