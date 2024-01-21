package com.galsie.gcs.resources.config.role;

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceGeneralLocalProperties;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.GCSMicroserviceAwarenessService;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceRole;
import com.galsie.gcs.resources.data.entity.roleestablishement.ResourcesMicroserviceInstanceRoleEntity;
import com.galsie.gcs.resources.repository.roleestablishment.ResourcesMicroserviceInstanceRoleEntityRepository;
import com.galsie.lib.utils.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class GCSResourcesMicroserviceRoleEstablishmentService {

    @Autowired
    GCSResourcesMicroserviceStateHolder gcsResourcesMicroserviceStateHolder;

    @Autowired
    GCSMicroserviceGeneralLocalProperties gcsMicroserviceGeneralLocalProperties;

    @Autowired
    ResourcesMicroserviceInstanceRoleEntityRepository resourcesMicroserviceInstanceRoleEntityRepo;

    @Autowired
    GCSMicroserviceAwarenessService gcsMicroserviceAwarenessService;

    @Getter
    boolean isCheckingEstablishedRoles;

    @Getter
    boolean isEstablishingThisAsManager;

    public void checkEstablishedRoles() {
        isCheckingEstablishedRoles = true;
        var managerResourceServiceOpt = resourcesMicroserviceInstanceRoleEntityRepo.findByRole(GCSResourceMicroserviceRole.MANAGER);

        var uniqueNameVersionInstanceID = getUniqueNameVersionInstanceId(gcsMicroserviceGeneralLocalProperties.getMicroserviceName()
                , gcsMicroserviceGeneralLocalProperties.getVersion(), gcsMicroserviceGeneralLocalProperties.getInstanceUniqueId());
        if(managerResourceServiceOpt.isEmpty()){
            log.warn("No manager role established yet");
            try {
                establishThisAsNewManager(uniqueNameVersionInstanceID);
                isCheckingEstablishedRoles = false;
                return;
            } catch (InterruptedException e) {
                log.error("Error while establishing this microservice as manager", e);
            }
        }
        var managerResourceService = managerResourceServiceOpt.get();
        var awarenessDTO = gcsMicroserviceAwarenessService.getMicroserviceAwarenessStatus(GCSMicroservice.RESOURCES, managerResourceService.getUniqueNameInstanceId());
        if(gcsResourcesMicroserviceStateHolder.getThisMicroserviceRole().equals(GCSResourceMicroserviceRole.MANAGER)){
            gcsInternalManagerSetup(managerResourceService, awarenessDTO, uniqueNameVersionInstanceID);
            return;
        }
        if(awarenessDTO.isEmpty()) {
            log.info("The microservice that is established as manager is not responding, establishing this microservice as manager");
            try {
                GCSResponse.removeEntityThrows(resourcesMicroserviceInstanceRoleEntityRepo, managerResourceService);
                establishThisAsNewManager(uniqueNameVersionInstanceID);
                isCheckingEstablishedRoles =false;
            } catch (InterruptedException e) {
                log.error("Error while establishing this microservice as manager ", e);
            }
        }

    }

    private void gcsInternalManagerSetup(ResourcesMicroserviceInstanceRoleEntity managerResourceService, Optional<GCSMicroserviceAwarenessStatusDTO> awarenessDTO, String uniqueNameVersionInstanceID) {
        if(managerResourceService.getUniqueNameInstanceId().equals(uniqueNameVersionInstanceID)){
            log.info(String.format("This microservice: %s, is already established as manager", uniqueNameVersionInstanceID));
        }else{
            if(awarenessDTO.isEmpty()){
                log.info( "The microservice that is established as manager is not responding, establishing this microservice as manager");
                try {
                    GCSResponse.removeEntityThrows(resourcesMicroserviceInstanceRoleEntityRepo, managerResourceService);
                    establishThisAsNewManager(uniqueNameVersionInstanceID);
                } catch (InterruptedException e) {
                    log.error("Error while establishing this microservice as manager ", e);
                }
            }else {
                log.info("The microservice that is established as manager is still responding, keeping this microservice as normal");
                gcsResourcesMicroserviceStateHolder.setThisMicroserviceRole(GCSResourceMicroserviceRole.NORMAL);
            }
        }
        isCheckingEstablishedRoles =false;
    }

    public void establishThisAsNewManager(String uniqueNameVersionInstanceID) throws InterruptedException {
        isEstablishingThisAsManager = true;
        var managerResourceServiceOpt = resourcesMicroserviceInstanceRoleEntityRepo.findByRole(GCSResourceMicroserviceRole.MANAGER);
        if(managerResourceServiceOpt.isEmpty()){
            log.warn("No manager role established yet, establishing this resource service: "+ uniqueNameVersionInstanceID +", as manager");
            var entity = ResourcesMicroserviceInstanceRoleEntity.builder()
                    .uniqueNameInstanceId(uniqueNameVersionInstanceID)
                    .role(GCSResourceMicroserviceRole.MANAGER)
                    .build();
            GCSResponse.saveEntityThrows(resourcesMicroserviceInstanceRoleEntityRepo, entity);
            var randomInt = (new Random().nextInt(30)) + 30;
//            var randomInt = (new Random().nextInt(171)) + 30;
            log.info(randomInt + " seconds delay before establishing this microservice as manager");
            Thread.sleep(randomInt * 1000);
            gcsResourcesMicroserviceStateHolder.setThisMicroserviceRole(GCSResourceMicroserviceRole.MANAGER);
            isEstablishingThisAsManager = false;
            checkEstablishedRoles();
            return;
        }
        var managerResourceService = managerResourceServiceOpt.get();
        isEstablishingThisAsManager = false;
        if(managerResourceService.getUniqueNameInstanceId().equals(uniqueNameVersionInstanceID)){
            log.info(String.format("This microservice: %s, is already established as manager", uniqueNameVersionInstanceID));
            gcsResourcesMicroserviceStateHolder.setThisMicroserviceRole(GCSResourceMicroserviceRole.MANAGER);
            return;
        }
        if(gcsResourcesMicroserviceStateHolder.getThisMicroserviceRole().equals(GCSResourceMicroserviceRole.MANAGER)){
            gcsResourcesMicroserviceStateHolder.setThisMicroserviceRole(GCSResourceMicroserviceRole.NORMAL);
        }
        checkEstablishedRoles();
    }


    private static String getUniqueNameVersionInstanceId(String microserviceName, String version, String instanceUniqueId) {
        return microserviceName + "." + version + "." + instanceUniqueId;
    }

}
