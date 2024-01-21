package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = AbstractProvidedAssetDTODeserializer.class)
public interface AbstractProvidedAssetDTO {
}
