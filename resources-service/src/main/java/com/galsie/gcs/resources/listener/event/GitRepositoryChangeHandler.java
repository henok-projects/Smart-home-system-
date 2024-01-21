package com.galsie.gcs.resources.listener.event;

import com.galsie.gcs.resources.event.GalAssetsGitRepositoryNewCommitAvailableEvent;
import com.galsie.gcs.resources.service.gitsync.GalAssetsGitRepositorySynchronizerService;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.OnGCSEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GitRepositoryChangeHandler implements GCSEventListener {

    @Autowired
    GalAssetsGitRepositorySynchronizerService galAssetsGitRepositorySynchronizerService;

    @OnGCSEvent
    public void handleGitRepositoryChange(GalAssetsGitRepositoryNewCommitAvailableEvent galAssetsGitRepositoryNewCommitAvailableEvent) throws Exception {
        log.info("New commit for GALASSETS found, starting sync");
        galAssetsGitRepositorySynchronizerService.startGitGalAssetSync();
    }

}