package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.countrycodes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.ProvidedAssetDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CountryCodesListProvidedAssetDTO extends ProvidedAssetDTO {
    @NotNull
    private String id;

    @NotNull
    @JsonProperty("assets_file_version")
    private String assetsFileVersion;

    @NotNull
    private List<CountryCodeDTO> countryCodes;

    @Override
    public boolean valid() {
        return true;
    }
}
