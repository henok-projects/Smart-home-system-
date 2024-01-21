package com.galsie.gcs.resources.service.assetgroup;


import com.galsie.gcs.resources.data.dto.AssetGroupLastUpdateInfoDTO;
import com.galsie.gcs.resources.data.dto.DataModelAssetProviderDTO;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.DataModelAssetProvider;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseErrorDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataModelProviderService {

    @Autowired
    AssetGroupGeneralService assetGroupGeneralService;

    @Autowired
    DataModelAssetProvider dataModelAssetProvider;

    public GCSResponse<DataModelAssetProviderDTO> getDataModel(){
        var assetGroup = dataModelAssetProvider.getLoadedAssetGroup();
        var entityOpt = assetGroupGeneralService.findByAssetGroupType(assetGroup.getAssetGroupType());
        if (entityOpt.isEmpty()){
            return GCSResponse.errorResponse(GCSResponseErrorDTO.ofMessage(GCSResponseErrorType.ENTITY_NOT_FOUND, "The AssetGroupEntity of type " + assetGroup.getAssetGroupType().name() + " was not found."));
        }
        var entity = entityOpt.get();
        if (!entity.getLastUpdate().equals(assetGroup.getLastUpdate())){
            return GCSResponse.errorResponse(GCSResponseErrorDTO.ofMessage(GCSResponseErrorType.MISMATCH_ERROR, "The LoadedAssetGroup version is " + assetGroup.getLastUpdate().toString() + " different than that in the database " + entity.getLastUpdate().toString() + ". Try restarting the service as the files must have been updated."));
        }
        return GCSResponse.response(new DataModelAssetProviderDTO(dataModelAssetProvider.getGalModel(),
                dataModelAssetProvider.getMtrModel(), new AssetGroupLastUpdateInfoDTO(entity.getLastUpdate(), entity.isLastUpdateRequired())));
    }


}
