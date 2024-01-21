package com.galsie.gcs.resources.service.providableassetdtos;

import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request.GetAbstractProvidableAssetDTOListRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request.GetAbstractProvidableAssetDTORequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request.GetProvidableAssetDTORequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.response.GetProvidableAssetDTOListResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.response.GetProvidableAssetDTOResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.resources.repository.sync.providableassetdtos.MicroserviceSubscribedProvidableAssetDTOEntityRepository;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.ProvidableAssetResponseErrorType.*;
import static com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.response.GetProvidableAssetDTOResponseDTO.responseError;
import static com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.response.GetProvidableAssetDTOResponseDTO.responseSuccess;

@Slf4j
@Service
public class MicroserviceProvidableAssetDTOsService {

    @Autowired
    MicroserviceSubscribedProvidableAssetDTOEntityRepository microserviceSubscribedProvidableAssetDTOEntityRepository;



    public GCSResponse<GetProvidableAssetDTOResponseDTO> getLoadedAssetDTO(GetAbstractProvidableAssetDTORequestDTO assetRequest) {
        try {
            if (!assetRequest.valid()) {
                return responseError(DTO_NOT_FOUND);
            }
            var providableAssetDTOType = assetRequest.getProvidableAssetDTOType().get();
            var loadedAssetGroup = LoadedAssetGroup.sharedInstance(providableAssetDTOType.getAssetGroupType());
            if (loadedAssetGroup == null) {
                return responseError(ASSET_GROUP_TYPE_NOT_FOUND);
            }
            var relativeFilePath = providableAssetDTOType.getPathRelativeToAssetGroupType();
            var assetFile = loadedAssetGroup.getLoadedFile(relativeFilePath);
            if (assetFile.isPresent()) {
                var providedAssetDTO = assetFile.get().toDTO(providableAssetDTOType.getProvidedAssetDTOClassType());
                if(providedAssetDTO.valid()) {
                    return responseSuccess(providedAssetDTO);
                }
            }
            return responseError(ASSET_FILE_NOT_FOUND);
        }catch (Exception e){
            log.error(e.getMessage());
            return responseError(FAILED_GETTING_LOADED_ASSET_GROUP);
        }
    }

    public GCSResponse<GetProvidableAssetDTOListResponseDTO> getLoadedAssetDTOs(GetAbstractProvidableAssetDTOListRequestDTO assetRequest) {
        if (!assetRequest.valid()) {
            return GetProvidableAssetDTOListResponseDTO.responseError(DTO_NOT_FOUND);
        }
        var providableAssetDTOTypes = (Set<ProvidableAssetDTOType>)assetRequest.getProvidableAssetDTOTypes();
        Map<Integer, AbstractProvidedAssetDTO> returnDTOs = new HashMap<>();
        int i = 0;
        for(ProvidableAssetDTOType providableAssetDTOType : providableAssetDTOTypes){
            var methodRequest = new GetProvidableAssetDTORequestDTO(providableAssetDTOType.getName());
            var response = getLoadedAssetDTO(methodRequest);
            if(response.hasResponseData()){
                var responseData = response.getResponseData();

                if(responseData.getProvidableAssetResponseError() == null){
                    returnDTOs.put(i , responseData.getProvidedAssetDTO());
                }
                else{
                    log.warn(responseData.getProvidableAssetResponseError().name()+ " "+providableAssetDTOType);
                }
            }
            i++;
        }
        return GetProvidableAssetDTOListResponseDTO.responseSuccess(returnDTOs);
    }

}
