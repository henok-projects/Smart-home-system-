package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

/**
 * Used by {@link GalAssetsSyncRequestDTO}
 */
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AssetGroupFileListSyncRequestDTO {

    @NotNull
    private AssetGroupType assetGroupType;

    @NotNull
    private List<AssetGroupFileSyncRequestDTO> filesToSync;


}
