package com.galsie.gcs.microservicecommon.lib.deserialize.providableassetdto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOWithTypeDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOWithType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DeserializerAbstractProvidableAssetDTOWithTypeDTO extends JsonDeserializer<AbstractProvidableAssetDTOWithTypeDTO> {

    @Override
    public AbstractProvidableAssetDTOWithTypeDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);
        if(node.has("providableAssetDTOType")){
            var providableAssetDTOType = mapper.treeToValue(node.get("providableAssetDTOType"), ProvidableAssetDTOType.class);
            if(node.has("providedAssetDTO")){
                var providedAssetDTO = mapper.treeToValue(node.get("providedAssetDTO"), AbstractProvidedAssetDTO.class);
                return ProvidableAssetDTOWithType.builder().providableAssetDTOType(providableAssetDTOType).providedAssetDTO(providedAssetDTO).build();
            }
        }
        log.error("Could not deserialize AbstractProvidableAssetDTOWithTypeDTO" + node);
        return null;
    }
}
