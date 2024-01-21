package com.galsie.gcs.resources.bootstrap;

import com.galsie.gcs.resources.config.role.GCSResourcesMicroserviceRoleEstablishmentService;
import com.galsie.gcs.resources.config.role.GCSResourcesMicroserviceStateHolder;
import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceRole;
import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceStatus;
import com.galsie.gcs.resources.data.dto.GitSyncUpdateMessageDTO;
import com.galsie.gcs.resources.repository.gitsync.GalResourceGitSynchronizationEntityRepository;
import com.galsie.gcs.resources.service.gitsync.GalAssetsGitRepositorySynchronizerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BootStrapRoleEstablishment implements InitializingBean {

    @Autowired
    GCSResourcesMicroserviceRoleEstablishmentService gcsResourcesMicroserviceRoleEstablishmentService;

    @Autowired
    GalAssetsGitRepositorySynchronizerService assetsGitRepositorySynchronizerService;

    @Autowired
    GalResourceGitSynchronizationEntityRepository galResourceGitSynchronizationEntityRepo;

    @Autowired
    GCSResourcesMicroserviceStateHolder gcsResourcesMicroserviceStateHolder;

    @Override
    public void afterPropertiesSet() throws Exception {
        gcsResourcesMicroserviceRoleEstablishmentService.checkEstablishedRoles();
        if(gcsResourcesMicroserviceStateHolder.getThisMicroserviceRole().equals(GCSResourceMicroserviceRole.MANAGER)){
            assetsGitRepositorySynchronizerService.startGitGalAssetSync();
        }else{
            int retries = 0;
            while(retries < 3){
                var gitSynchronizationEntities = galResourceGitSynchronizationEntityRepo.findAll();
                if(gitSynchronizationEntities.isEmpty() ||  gitSynchronizationEntities.get(0).getResourceManagerStatus().equals(GCSResourceMicroserviceStatus.STARTING_UP)) {
                    Thread.sleep(1000L * 60 * (retries + 1));
                    retries++;
                    continue;
                }
                var gitSynchronizationEntity = gitSynchronizationEntities.get(0);
                var gitSyncUpdateMessageDTO = GitSyncUpdateMessageDTO.builder().status(gitSynchronizationEntity.getResourceManagerStatus()).build();
                if(gitSynchronizationEntity.getResourceManagerStatus().equals(GCSResourceMicroserviceStatus.MANAGER_WAITING_FOR_INSTANCES_TO_SYNC) ||
                        gitSynchronizationEntity.getResourceManagerStatus().equals(GCSResourceMicroserviceStatus.RUNNING)){
                    assetsGitRepositorySynchronizerService.updateStatusFromGitSyncUpdateMessage(gitSyncUpdateMessageDTO);
                    return;
                }
                if(gitSynchronizationEntity.getResourceManagerStatus().equals(GCSResourceMicroserviceStatus.SYNCHRONIZING_RESOURCES)){
                    assetsGitRepositorySynchronizerService.updateStatusFromGitSyncUpdateMessage(gitSyncUpdateMessageDTO);
                    return;
                }
                retries++;
            }
            afterPropertiesSet();
        }
    }
}
