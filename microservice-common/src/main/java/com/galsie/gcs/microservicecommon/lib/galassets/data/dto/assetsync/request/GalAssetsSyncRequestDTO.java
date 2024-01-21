package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.Nullable;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is used to make a request to resources service to sync assets using combination of either lastSyncTime or
 * assetsToSync or lastSyncTime and assetsToSync
 * the requesting service gets a AssetsSyncResponseDTO response, providing the syncId, total files to sync and total pages
 */
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GalAssetsSyncRequestDTO {

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSyncTime;

    @Nullable
    private List<AssetGroupFileListSyncRequestDTO> assetsToSync;


}
