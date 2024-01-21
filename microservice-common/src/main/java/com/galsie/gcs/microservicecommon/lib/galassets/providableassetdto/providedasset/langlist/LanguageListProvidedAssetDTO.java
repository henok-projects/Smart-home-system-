package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.langlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.ProvidedAssetDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LanguageListProvidedAssetDTO extends ProvidedAssetDTO {

    private Long id;

    @JsonProperty("assets_file_version")
    private String assetsFileVersion;

    List<Language> languages;

    @Override
    public boolean valid() {
        return id != null && !assetsFileVersion.isEmpty() && !languages.isEmpty();
    }
}
