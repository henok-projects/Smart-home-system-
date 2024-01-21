package com.galsie.gcs.resources.repository.sync.assetsync;

import com.galsie.gcs.resources.data.entity.sync.GalAssetsSynchronizationEntity;
import com.galsie.gcs.resources.data.entity.sync.GalAssetsSynchronizedFileEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GalAssetsSynchronizedFileEntityRepository extends GalRepository<GalAssetsSynchronizedFileEntity, Long> {

    List<GalAssetsSynchronizedFileEntity> findAllByModificationDateBefore(LocalDateTime timeStamp);

    Integer deleteAllByModificationDateBefore(LocalDateTime timeStamp);

    List<GalAssetsSynchronizedFileEntity>  findAllByGalAssetsSynchronizationEntityAndPage(GalAssetsSynchronizationEntity galAssetsSynchronizationEntity, Long page);

}
