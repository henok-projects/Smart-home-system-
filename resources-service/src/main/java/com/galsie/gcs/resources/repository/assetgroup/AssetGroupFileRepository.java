package com.galsie.gcs.resources.repository.assetgroup;


import com.galsie.gcs.resources.data.entity.assetgroup.AssetGroupEntity;
import com.galsie.gcs.resources.data.entity.assetgroup.AssetGroupFileEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AssetGroupFileRepository extends GalRepository<AssetGroupFileEntity, Long> {

    List<AssetGroupFileEntity> findAllByUpdatedAtAfter(LocalDateTime updateTime);

    List<AssetGroupFileEntity> findAllByAssetGroupAndPathIn(AssetGroupEntity assetGroupEntity, List<String> paths);

    List<AssetGroupFileEntity> findAllByAssetGroupAndPathInAndUpdatedAtAfter(AssetGroupEntity assetGroupEntity, List<String> path, LocalDateTime updateTime);
}
