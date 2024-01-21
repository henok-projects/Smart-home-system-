package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.*;

/**
 * Used by {@link AssetGroupFileListSyncRequestDTO}
 */
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AssetGroupFileSyncRequestDTO {

    @NotNull
    private String path;

    @Nullable
    private String version;

}
