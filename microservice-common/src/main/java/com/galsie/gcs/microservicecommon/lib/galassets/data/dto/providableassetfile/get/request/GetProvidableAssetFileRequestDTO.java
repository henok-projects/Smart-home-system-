package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetfile.get.request;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

/**
 * This class is used to request a file from the resources service, which can in turn can return a response of
 * GetProvidableLoadedAssetFileResponseDTO or GetProvidableLoadedAssetFileAsStringResponseDTO
 */
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetProvidableAssetFileRequestDTO {

    @NotNull
    private AssetGroupType assetGroupType;

    @NotNull
    private String path;

    @NotNull
    private Boolean includeVersion;

}
