package com.galsie.gcs.resources.event;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventCommonImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GalAssetsGitRepositoryNewCommitAvailableEvent extends GCSEventCommonImpl {

    private final String latestCommitSHA;

    @Override
    public boolean isCancellable() {
        return false;
    }

}
