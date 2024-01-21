package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.galsie.gcs.microservicecommon.lib.deserialize.providableassetdto.DeserializerAbstractProvidableAssetDTOWithTypeDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;

@JsonDeserialize(using = DeserializerAbstractProvidableAssetDTOWithTypeDTO.class)
public interface AbstractProvidableAssetDTOWithTypeDTO {

    AbstractProvidableAssetDTOType getProvidableAssetDTOType();

    AbstractProvidedAssetDTO getProvidedAssetDTO();

}
