package com.galsie.gcs.resources.service.assetsync;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.resources.data.dto.syncentitycleanup.GalAssetsSynchronizationEntityCleanupResponseDTO;
import com.galsie.gcs.resources.data.entity.sync.GalAssetsSynchronizedFileEntity;
import com.galsie.gcs.resources.repository.sync.assetsync.GalAssetsSynchronizationEntityRepository;
import com.galsie.gcs.resources.repository.sync.assetsync.GalAssetsSynchronizedFileEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

@Service
public class GalAssetsSynchronizationEntityCleanupService {

    @Autowired
    GalAssetsSynchronizedFileEntityRepository galAssetsSynchronizedFileEntityRepository;

    @Autowired
    GalAssetsSynchronizationEntityRepository galAssetsSynchronizationEntityRepository;

    public GCSResponse<GalAssetsSynchronizationEntityCleanupResponseDTO> cleanupAssetFileSyncedRepo(LocalDateTime timeStamp) {
        try {
            return gcsInternalCleanupAssetFileSyncedRepo(timeStamp);
        }catch (Exception e) {
            return GalAssetsSynchronizationEntityCleanupResponseDTO.responseError(e.getMessage());
        }
    }

    @Transactional
    private GCSResponse<GalAssetsSynchronizationEntityCleanupResponseDTO> gcsInternalCleanupAssetFileSyncedRepo(LocalDateTime timeStamp) throws GCSResponseException {
        var galAssetSynchronizationEntityList = galAssetsSynchronizedFileEntityRepository.findAllByModificationDateBefore(timeStamp).stream().map(GalAssetsSynchronizedFileEntity::getGalAssetsSynchronizationEntity).distinct().toList();
        var numberDeleted = galAssetsSynchronizedFileEntityRepository.deleteAllByModificationDateBefore(timeStamp);
        galAssetsSynchronizationEntityRepository.deleteAllInBatch(galAssetSynchronizationEntityList);
        return GalAssetsSynchronizationEntityCleanupResponseDTO.responseSuccess(numberDeleted);
    }

}
