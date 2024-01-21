package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.galassets.versionchanged;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GalAssetsSomeFileVersionChangedBroadcastDTO {

    @NotNull
    private String path;

    @NotNull
    private String version;

    @NotNull
    private AssetGroupType assetGroupType;

}
