package com.galsie.gcs.resources.repository.assetgroup;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.resources.data.entity.assetgroup.AssetGroupEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.util.Optional;

public interface AssetGroupRepository extends GalRepository<AssetGroupEntity, Long> {

    Optional<AssetGroupEntity> findByAssetGroupType(AssetGroupType assetGroupType);

}
