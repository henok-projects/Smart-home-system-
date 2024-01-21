package com.galsie.gcs.microservicecommon.lib.galassets;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class GCSGalAssetsConfigurationDataProvider {

    private Set<ProvidableAssetDTOType> subscribableProvidedAssetDTOTypes;

    private Set<ProvidableAssetDTOType> StartUpProvidableAssetDTOTypes;

}
