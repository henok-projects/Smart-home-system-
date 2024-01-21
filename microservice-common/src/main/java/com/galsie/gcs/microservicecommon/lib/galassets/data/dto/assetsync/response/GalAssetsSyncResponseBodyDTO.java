package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Used by {@link GalAssetsSyncResponseDTO} to keep information about the response to asset sync file request
 */
@GalDTO
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GalAssetsSyncResponseBodyDTO {

    @Nullable
    @JsonProperty("sync_id")
    private Long syncId;

    @NotNull
    @JsonProperty("files_to_sync_count")
    private Integer filesToSyncCount;

    @NotNull
    @JsonProperty("page_count")
    private Integer pageCount;


}
