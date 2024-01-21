package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO that contains information about a file that is being synchronized with the client
 * used by {@link GetSynchronizedPageResponseDTO}
 */
@GalDTO
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AssetGroupFileSyncedDataDTO {

    @NotNull
    private AssetGroupType assetGroupType;

    @NotNull  
    private String path;

    @NotNull
    private String version;

    @JsonProperty("file_data")
    private String fileData;


}
