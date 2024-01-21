package com.galsie.gcs.resources.service.assetgroup;

import com.galsie.gcs.resources.data.dto.AssetGroupLastUpdateInfoDTO;
import com.galsie.gcs.resources.data.entity.assetgroup.AssetGroupEntity;
import com.galsie.gcs.resources.repository.assetgroup.AssetGroupFileRepository;
import com.galsie.gcs.resources.repository.assetgroup.AssetGroupRepository;
import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseErrorDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.lib.utils.OptionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssetGroupGeneralService {

    @Autowired
    AssetGroupRepository assetGroupRepository;

    @Autowired
    AssetGroupFileRepository assetGroupFileRepository;

    public Optional<AssetGroupEntity> findByAssetGroupType(AssetGroupType assetGroupType){
        return assetGroupRepository.findByAssetGroupType(assetGroupType);
    }

    public GCSResponse<?> getLastUpdateInfo(String assetGroupTypeName){
        var typeOpt = OptionalUtils.ofThrowable(() -> AssetGroupType.valueOf(assetGroupTypeName));
        if (typeOpt.isEmpty()){
            return GCSResponse.errorResponseWithMessage(GCSResponseErrorType.TYPE_NOT_FOUND, "The type " + assetGroupTypeName + " was not found");
        }
        return getLastUpdateInfo(typeOpt.get());
    }

    public GCSResponse<AssetGroupLastUpdateInfoDTO> getLastUpdateInfo(AssetGroupType assetGroupType){
        var assetGroupOpt = findByAssetGroupType(assetGroupType);
        if (assetGroupOpt.isEmpty()){
            return GCSResponse.errorResponse(GCSResponseErrorDTO.ofMessage(GCSResponseErrorType.ENTITY_NOT_FOUND, "The AssetGroupEntity of type " + assetGroupType.name() + " was not found."));
        }
        var assetGroup = assetGroupOpt.get();
        return GCSResponse.response(new AssetGroupLastUpdateInfoDTO(assetGroup.getLastUpdate(), assetGroup.isLastUpdateRequired()));
    }
}
