package com.galsie.gcs.resources.service.providableassetfile;

import com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset.LoadedAssetFile;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetfile.get.request.GetProvidableAssetFileRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetfile.get.response.GetProvidableLoadedAssetFileResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetfile.get.response.ProvidableLoadedAssetStringResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.ProvidableAssetResponseErrorType.*;

@Service
@Slf4j
public class ProvidableLoadedAssetFileService {

    public GCSResponse<GetProvidableLoadedAssetFileResponseDTO> getLoadedAssetFile(GetProvidableAssetFileRequestDTO getProvidableAssetFileRequestDTO) {
        try {
            var loadedAssetGroup = LoadedAssetGroup.sharedInstance(getProvidableAssetFileRequestDTO.getAssetGroupType());
            if (loadedAssetGroup == null) {
                return GetProvidableLoadedAssetFileResponseDTO.errorResponse(ASSET_GROUP_TYPE_NOT_FOUND);
            }

            var assetFile = getLoadedAssetFileFromAssetGroup(loadedAssetGroup, getProvidableAssetFileRequestDTO.getPath());
            return assetFile.map(GetProvidableLoadedAssetFileResponseDTO::responseSuccess).orElseGet(() -> GetProvidableLoadedAssetFileResponseDTO.errorResponse(ASSET_FILE_NOT_FOUND));

        }catch (Exception e){
            return GetProvidableLoadedAssetFileResponseDTO.errorResponse(FAILED_GETTING_LOADED_ASSET_GROUP);
        }
    }

    public GCSResponse<ProvidableLoadedAssetStringResponseDTO> getLoadedAssetFileAsString(GetProvidableAssetFileRequestDTO getProvidableAssetFileRequestDTO) {
        try {
            var loadedAssetGroup = LoadedAssetGroup.sharedInstance(getProvidableAssetFileRequestDTO.getAssetGroupType());
            if (loadedAssetGroup == null) {
                return ProvidableLoadedAssetStringResponseDTO.responseError(ASSET_GROUP_TYPE_NOT_FOUND);
            }

            var assetFile = getLoadedAssetFileFromAssetGroup(loadedAssetGroup, getProvidableAssetFileRequestDTO.getPath());
            if (assetFile.isPresent()) {
                var encodedString =  assetFile.get().getBase64EncodedData(getProvidableAssetFileRequestDTO.getIncludeVersion());
                if(encodedString == null){
                    return ProvidableLoadedAssetStringResponseDTO.responseError(ENCODING_ERROR);
                }
                return  ProvidableLoadedAssetStringResponseDTO.responseSuccess(encodedString);
            }

            return ProvidableLoadedAssetStringResponseDTO.responseError(ASSET_FILE_NOT_FOUND);
        }catch (Exception e){
            return ProvidableLoadedAssetStringResponseDTO.responseError(FAILED_GETTING_LOADED_ASSET_GROUP);
        }
    }

    private Optional<LoadedAssetFile> getLoadedAssetFileFromAssetGroup(LoadedAssetGroup loadedAssetGroup, String path) {
        var loadedAssetFileOpt = loadedAssetGroup.getLoadedFile(path);
        if (loadedAssetFileOpt.isEmpty()) {
            var actualPath = loadedAssetGroup.getActualPath(path);
            if (actualPath == null) {
                return Optional.empty();
            }
            loadedAssetFileOpt = loadedAssetGroup.getLoadedFile(actualPath);
        }
        return loadedAssetFileOpt;
    }

}
