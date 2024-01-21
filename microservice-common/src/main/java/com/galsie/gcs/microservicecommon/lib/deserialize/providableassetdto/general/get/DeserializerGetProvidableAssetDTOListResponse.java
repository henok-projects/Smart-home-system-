package com.galsie.gcs.microservicecommon.lib.deserialize.providableassetdto.general.get;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.ProvidableAssetResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.response.GetProvidableAssetDTOListResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;

import java.io.IOException;
import java.util.HashMap;

public class DeserializerGetProvidableAssetDTOListResponse extends JsonDeserializer<GetProvidableAssetDTOListResponseDTO> {

    @Override
    public GetProvidableAssetDTOListResponseDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);
        var errorNode = node.get("providableAssetResponseError");
        ProvidableAssetResponseErrorType error = mapper.treeToValue(errorNode, ProvidableAssetResponseErrorType.class);
        var providedAssetDTOsNode = node.get("providedAssetDTOs");
        var providedAssetDTOs = mapper.readValue(providedAssetDTOsNode.toString(), new TypeReference<HashMap<Integer, AbstractProvidedAssetDTO>>(){});
        return new GetProvidableAssetDTOListResponseDTO(error, providedAssetDTOs);
    }
}
