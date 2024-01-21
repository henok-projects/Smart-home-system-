package com.galsie.gcs.resources.config.git;

import com.galsie.gcs.resources.config.role.GCSResourcesMicroserviceStateHolder;
import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceRole;
import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceStatus;
import com.galsie.gcs.resources.event.GalAssetsGitRepositoryNewCommitAvailableEvent;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResourcesGalAssetsGitRepositoryNewCommitCheckerScheduledTasks {

    @Autowired
    ResourcesGalAssetGitOperations resourcesGalAssetGitOperations;

    @Autowired
    GCSEventManager eventManager;

    @Autowired
    GCSResourcesMicroserviceStateHolder gcsResourcesMicroserviceStateHolder;

    @Scheduled(fixedDelay = 5 * 60 * 1000) //Checking every 5 mins
    public void checkRepositoryForNewCommit(){
        if(gcsResourcesMicroserviceStateHolder.getThisMicroserviceRole().equals(GCSResourceMicroserviceRole.NORMAL)){
            return;
        }
        if(!gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus().equals(GCSResourceMicroserviceStatus.RUNNING)){
            return;
        }
        var localLastCommitSHAOpt = resourcesGalAssetGitOperations.checkLocalRepoLastCommitSha();
        if(localLastCommitSHAOpt.isEmpty()){
            log.warn("Failed to get local last commit SHA");
            return;
        }
        var onlineLastCommitSHAOpt = resourcesGalAssetGitOperations.checkOnlineRepoLastCommitSHA();
        if(onlineLastCommitSHAOpt.isEmpty()){
            log.warn("Failed to get online last commit SHA");
            return;
        }
        if(localLastCommitSHAOpt.get().equals(onlineLastCommitSHAOpt.get())){
            log.info("No new commit found");
            return;
        }
        eventManager.callEvent( new GalAssetsGitRepositoryNewCommitAvailableEvent(onlineLastCommitSHAOpt.get()));
    }

}
