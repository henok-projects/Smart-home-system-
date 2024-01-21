package com.galsie.gcs.resources.service.gitsync;

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceGeneralLocalProperties;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import com.galsie.gcs.resources.bootstrap.BootstrapBean;
import com.galsie.gcs.resources.config.role.GCSResourcesMicroserviceStateHolder;
import com.galsie.gcs.resources.config.git.ResourcesGalAssetGitOperations;
import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceRole;
import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceStatus;
import com.galsie.gcs.resources.data.dto.GitSyncUpdateMessageDTO;
import com.galsie.gcs.resources.data.entity.gitsync.GalResourceGitSynchronizationEntity;
import com.galsie.gcs.resources.repository.gitsync.GalResourceGitSynchronizationEntityRepository;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class GalAssetsGitRepositorySynchronizerService {

    @Autowired
    GCSResourcesMicroserviceStateHolder gcsResourcesMicroserviceStateHolder;

    @Autowired
    GalResourceGitSynchronizationEntityRepository galResourceGitSynchronizationEntityRepo;

    @Autowired
    ResourcesGalAssetGitOperations resourcesGalAssetGitOperations;

    @Autowired
    BootstrapBean bootstrapBean;

    @Autowired
    TaskScheduler taskScheduler;

    private ScheduledFuture<?> scheduledFuture;

    @Autowired
    GCSRabbitTemplate gcsRabbitTemplate;

    @Autowired
    GCSMicroserviceGeneralLocalProperties gcsMicroserviceGeneralLocalProperties;

    private String syncRandomUID;

    public void startGitGalAssetSync() throws Exception {
        if(!gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus().equals(GCSResourceMicroserviceStatus.RUNNING) &&
                !gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus().equals(GCSResourceMicroserviceStatus.STARTING_UP)){
            log.error("This resource microservice is neither running nor starting up");
            return;
        }
        var currentCommitSHAOpt = resourcesGalAssetGitOperations.checkOnlineRepoLastCommitSHA();
        var lastLocalCommitSHAOpt = resourcesGalAssetGitOperations.checkLocalRepoLastCommitSha();
        if(currentCommitSHAOpt.isEmpty()){
            log.warn("Failed to get current commit SHA");
            return;
        }
        if(lastLocalCommitSHAOpt.isEmpty()){
            log.warn("Failed to get last local commit SHA");
            return;
        }
        var gcsMicroserviceOpt = gcsMicroserviceGeneralLocalProperties.getGCSMicroservice();
        if(gcsMicroserviceOpt.isEmpty()){
            log.warn("Failed to get microservice name");
            return;
        }
        var galResourceGitSynchronizationEntity = GalResourceGitSynchronizationEntity.builder()
                .gcsMicroservice(gcsMicroserviceOpt.get())
                .instanceId(gcsMicroserviceGeneralLocalProperties.getInstanceUniqueId())
                .currentCommitSHA(currentCommitSHAOpt.get()).build();
        galResourceGitSynchronizationEntityRepo.deleteAll();
        if(lastLocalCommitSHAOpt.get().equals(currentCommitSHAOpt.get())){
            if(gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus().equals(GCSResourceMicroserviceStatus.STARTING_UP)) {
                galResourceGitSynchronizationEntity.setResourceManagerStatus(GCSResourceMicroserviceStatus.STARTING_UP);
                GCSResponse.saveEntitiesThrows(galResourceGitSynchronizationEntityRepo, galResourceGitSynchronizationEntity);
                resourcesGalAssetGitOperations.pullChanges();
                bootstrapBean.bootstrap();
                gcsResourcesMicroserviceStateHolder.setThisMicroserviceStatus(GCSResourceMicroserviceStatus.RUNNING);
                galResourceGitSynchronizationEntity.setResourceManagerStatus(gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus());
                GCSResponse.saveEntitiesThrows(galResourceGitSynchronizationEntityRepo, galResourceGitSynchronizationEntity);
            }
            log.info("No new commit found");
            return;
        }
        gcsResourcesMicroserviceStateHolder.setThisMicroserviceStatus(GCSResourceMicroserviceStatus.SYNCHRONIZING_RESOURCES);
        galResourceGitSynchronizationEntity.setResourceManagerStatus(GCSResourceMicroserviceStatus.SYNCHRONIZING_RESOURCES);
        GCSResponse.saveEntitiesThrows(galResourceGitSynchronizationEntityRepo, galResourceGitSynchronizationEntity);
        broadcastSyncUpdate();
        Thread.sleep(1000 * 30); //Wait for 30 seconds to allow other microservices to receive the update
        resourcesGalAssetGitOperations.pullChanges();
        var newCommitSHAOpt = resourcesGalAssetGitOperations.checkLocalRepoLastCommitSha();
        if(newCommitSHAOpt.isEmpty()){
            log.error("Failed to get new commit SHA");
            return;
        }
        galResourceGitSynchronizationEntity.setCurrentCommitSHA(newCommitSHAOpt.get());
        GCSResponse.saveEntitiesThrows(galResourceGitSynchronizationEntityRepo, galResourceGitSynchronizationEntity);
        LoadedAssetGroup.clearCache();
        bootstrapBean.bootstrap();
        gcsResourcesMicroserviceStateHolder.setThisMicroserviceStatus(GCSResourceMicroserviceStatus.MANAGER_WAITING_FOR_INSTANCES_TO_SYNC);
        GCSResponse.saveEntitiesThrows(galResourceGitSynchronizationEntityRepo, galResourceGitSynchronizationEntity);
        broadcastSyncUpdate();
        log.info("Broadcast done, about to sleep");
        Thread.sleep(1000 * 60 * 2); //Wait for 2 minutes to allow other microservices to receive the update
        log.info("Done sleeping, about to end sync");
        endGitGalAssetsSync();
    }

    @Transactional
    public void endGitGalAssetsSync() throws GCSResponseException {
        if(gcsResourcesMicroserviceStateHolder.getThisMicroserviceRole().equals(GCSResourceMicroserviceRole.MANAGER)){
            gcsInternalEndGitGalAssetsSyncManager();
            return;
        }
        if(!gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus().equals(GCSResourceMicroserviceStatus.INSTANCES_WAITING_FOR_MANAGER_TO_SYNC)){
            log.info("This service is not manager and is not in right state to end sync");
            return;
        }
        gcsInternalEndGitGalAssetsSyncNormal();
    }

    public void timeoutGitGalAssetsSync(){
        if(syncRandomUID.isEmpty()){
            log.error("syncRandomUID is empty");
            return;
        }
        endGitGalAssetsSync();
    }

    public void updateStatusFromGitSyncUpdateMessage(GitSyncUpdateMessageDTO gitSyncUpdateMessageDTO){
        if(gcsResourcesMicroserviceStateHolder.getThisMicroserviceRole().equals(GCSResourceMicroserviceRole.MANAGER)){
            log.info("This service is manager and should not respond to such messages");
            return;
        }
        if(gitSyncUpdateMessageDTO.getStatus().equals(GCSResourceMicroserviceStatus.SYNCHRONIZING_RESOURCES)){
            gcsResourcesMicroserviceStateHolder.setThisMicroserviceStatus(GCSResourceMicroserviceStatus.INSTANCES_WAITING_FOR_MANAGER_TO_SYNC);
            var localSyncId = UUID.randomUUID().toString();
            syncRandomUID = localSyncId;
            triggerSyncRandomUIDCheck(localSyncId);
            return;
        }
        if(gitSyncUpdateMessageDTO.getStatus().equals(GCSResourceMicroserviceStatus.MANAGER_WAITING_FOR_INSTANCES_TO_SYNC) ||
                gitSyncUpdateMessageDTO.getStatus().equals(GCSResourceMicroserviceStatus.RUNNING)){
            endGitGalAssetsSync();
        }

    }


    private void gcsInternalEndGitGalAssetsSyncManager() throws GCSResponseException {
        if(!gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus().equals(GCSResourceMicroserviceStatus.MANAGER_WAITING_FOR_INSTANCES_TO_SYNC)) {
            log.info("Resources manager is not in the right state to end sync");
            return;
        }
        var gcsMicroserviceOpt = gcsMicroserviceGeneralLocalProperties.getGCSMicroservice();
        if(gcsMicroserviceOpt.isEmpty()) {
            log.info("endGitGalAssetsSync cannot proceed because microservice name is invalid");
        }
        gcsResourcesMicroserviceStateHolder.setThisMicroserviceStatus(GCSResourceMicroserviceStatus.RUNNING);
        var gitSyncEntityOpt = galResourceGitSynchronizationEntityRepo.findByGcsMicroserviceAndInstanceId(
                gcsMicroserviceOpt.get(),
                gcsMicroserviceGeneralLocalProperties.getInstanceUniqueId());
        if(gitSyncEntityOpt.isEmpty()) {
            log.info("endGitGalAssetsSync cannot proceed because git sync entity is not found");
            return;
        }
        var gitSyncEntity = gitSyncEntityOpt.get();
        gitSyncEntity.setResourceManagerStatus(gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus());
        GCSResponse.saveEntitiesThrows(galResourceGitSynchronizationEntityRepo, gitSyncEntity);
    }

    private void gcsInternalEndGitGalAssetsSyncNormal(){
        var latestCommitSHAOpt = resourcesGalAssetGitOperations.checkOnlineRepoLastCommitSHA();
        if(latestCommitSHAOpt.isEmpty()){
            log.error("Failed to get latest online commit SHA");
            return;
        }
        var galResourceGitSynchronizationEntityOpt = galResourceGitSynchronizationEntityRepo.findByCurrentCommitSHA(latestCommitSHAOpt.get());
        if(galResourceGitSynchronizationEntityOpt.isEmpty()){
            log.error("Failed to find git sync entity with latest commit SHA");
            return;
        }
        gcsResourcesMicroserviceStateHolder.setThisMicroserviceStatus(GCSResourceMicroserviceStatus.SYNCHRONIZING_RESOURCES);
        LoadedAssetGroup.clearCache();
        resourcesGalAssetGitOperations.pullChanges();
        syncRandomUID = "";
        gcsResourcesMicroserviceStateHolder.setThisMicroserviceStatus(GCSResourceMicroserviceStatus.RUNNING);
    }

    private void broadcastSyncUpdate() {
        gcsRabbitTemplate.convertAndSend(GCSRabbitMQCommonQueueType.RESOURCE_GIT_SYNC,
                GitSyncUpdateMessageDTO.builder().status(gcsResourcesMicroserviceStateHolder.getThisMicroserviceStatus()).build());
    }

    private void triggerSyncRandomUIDCheck(String localSyncId){
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        scheduledFuture = taskScheduler.schedule(
                () -> syncRandomUIDCheck(localSyncId),
                new Date(System.currentTimeMillis() +
                        (1000 *  60 * 5)) // 5 minutes delay
        );
    }

    private void syncRandomUIDCheck(String localSyncId){

        if(syncRandomUID.equals(localSyncId)){
            timeoutGitGalAssetsSync();
        }
    }

}
