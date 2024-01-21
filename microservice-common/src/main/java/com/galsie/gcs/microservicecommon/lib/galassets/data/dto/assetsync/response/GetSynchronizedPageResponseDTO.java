package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.assetsync.GetSynchronizedPageErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@GalDTO
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GetSynchronizedPageResponseDTO {

    @Nullable
    GetSynchronizedPageErrorType assetErrorType;

    @Nullable
    @JsonProperty("sync_info")
    List<AssetGroupFileSyncedDataDTO> syncInfo;

    private static GetSynchronizedPageResponseDTO success(List<AssetGroupFileSyncedDataDTO> assetGroupFileSyncedDataDTOS){
        return  GetSynchronizedPageResponseDTO.builder().syncInfo(assetGroupFileSyncedDataDTOS).build();
    }

    private static GetSynchronizedPageResponseDTO error(GetSynchronizedPageErrorType assetErrorType){
        return  GetSynchronizedPageResponseDTO.builder().assetErrorType(assetErrorType).build();
    }

    public static GCSResponse<GetSynchronizedPageResponseDTO> responseSuccess(List<AssetGroupFileSyncedDataDTO> assetGroupFileSyncedDataDTOS){
        return  GCSResponse.response(success(assetGroupFileSyncedDataDTOS));
    }

    public static GCSResponse<GetSynchronizedPageResponseDTO> responseError(GetSynchronizedPageErrorType assetErrorType){
        return  GCSResponse.response(error(assetErrorType));
    }
}
