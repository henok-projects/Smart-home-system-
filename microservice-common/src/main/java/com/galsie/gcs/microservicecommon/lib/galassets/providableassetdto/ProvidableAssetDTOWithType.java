package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@GalDTO
@AllArgsConstructor
@Getter
@Builder
public class ProvidableAssetDTOWithType implements AbstractProvidableAssetDTOWithTypeDTO{

    private ProvidableAssetDTOType providableAssetDTOType;

    private AbstractProvidedAssetDTO providedAssetDTO;

}
