package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.assetsync.GalAssetsSyncResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@GalDTO
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GalAssetsSyncResponseDTO {

    @Nullable
    @JsonProperty("sync_assets_error")
    private GalAssetsSyncResponseErrorType galAssetsSyncResponseErrorType;

    @Nullable
    @JsonProperty("sync_info")
    private GalAssetsSyncResponseBodyDTO syncInfo;

    private static GalAssetsSyncResponseDTO success(Long sync_id, Integer filesNumber, Integer noOfPages){
        GalAssetsSyncResponseBodyDTO assetSyncResponseBody = GalAssetsSyncResponseBodyDTO.builder().filesToSyncCount(filesNumber).pageCount(noOfPages)
                .syncId(sync_id).build();
         return GalAssetsSyncResponseDTO.builder().syncInfo(assetSyncResponseBody).build();
    }

    private static GalAssetsSyncResponseDTO error(GalAssetsSyncResponseErrorType galAssetsSyncResponseErrorType){
        return GalAssetsSyncResponseDTO.builder().galAssetsSyncResponseErrorType(galAssetsSyncResponseErrorType).build();
    }

    public static GCSResponse<GalAssetsSyncResponseDTO> responseSuccess(Long sync_id, Integer filesNumber, Integer noOfPages){
        return GCSResponse.response(success(sync_id, filesNumber, noOfPages));
    }


    public static GCSResponse<GalAssetsSyncResponseDTO> responseError(GalAssetsSyncResponseErrorType galAssetsSyncResponseErrorType) {
        return GCSResponse.response(error(galAssetsSyncResponseErrorType));
    }


}
