package com.galsie.gcs.resources.service.assetsync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class GalAssetsSynchronizationEntityCleanupScheduledTasks {

    @Autowired
    GalAssetsSynchronizationEntityCleanupService entityCleanupService;

    @Value("${galsie.assetsync.delete-synchronization-entities-older-than-days}")
    private long days;

    @Scheduled(fixedDelayString =  "${galsie.scheduled-tasks.clean-up-assetsync-entities-older-than-days-in-milli}")
    public void cleanupAssetFileSyncedRepo() {
        var timeStamp = LocalDateTime.now().minusDays(days);
        var response = entityCleanupService.cleanupAssetFileSyncedRepo(timeStamp).getResponseData();
        if(response.hasError()){
            log.error("Error while cleaning up asset file synchronization entities: {}", response.getErrorMessage());
            return;
        }
        log.info("Asset file synchronization entities cleanup completed successfully. Number of deleted entities: {}", response.getNoOfDeletedFiles());

    }
}
