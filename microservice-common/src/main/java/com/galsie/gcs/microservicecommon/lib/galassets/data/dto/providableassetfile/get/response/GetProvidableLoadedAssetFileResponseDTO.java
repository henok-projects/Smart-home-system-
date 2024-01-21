package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetfile.get.response;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.ProvidableAssetResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset.LoadedAssetFile;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetProvidableLoadedAssetFileResponseDTO<T extends LoadedAssetFile> {
    @Nullable
    private ProvidableAssetResponseErrorType providableAssetResponseError;

    @Nullable
    private T loadedAssetFile;


    public static GetProvidableLoadedAssetFileResponseDTO success(LoadedAssetFile loadedAssetFile){
        return GetProvidableLoadedAssetFileResponseDTO.builder().loadedAssetFile(loadedAssetFile).build();
    }

    public static GetProvidableLoadedAssetFileResponseDTO error(ProvidableAssetResponseErrorType responseError){
        return GetProvidableLoadedAssetFileResponseDTO.builder().providableAssetResponseError(responseError).build();
    }

    public static GCSResponse<GetProvidableLoadedAssetFileResponseDTO> responseSuccess(LoadedAssetFile assetFile){
        return GCSResponse.response(success(assetFile));
    }

    public static GCSResponse<GetProvidableLoadedAssetFileResponseDTO> errorResponse(ProvidableAssetResponseErrorType responseError){
        return GCSResponse.response(error(responseError));
    }

}
