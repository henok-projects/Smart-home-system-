package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.areaconfig;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.ProvidedAssetDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;

import java.util.Map;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AreaConfigurationProvidedAssetDTO extends ProvidedAssetDTO {

    @JsonProperty("assets_file_version")
    private String assetsFileVersion;

    @JsonProperty("initial_colors")
    private Map<String, String> initialColors;

    @Override
    public boolean valid() {
        return assetsFileVersion != null && initialColors != null;
    }
}
