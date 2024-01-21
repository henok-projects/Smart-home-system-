package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.areaconfig.AreaConfigurationProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.countrycodes.CountryCodeDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.countrycodes.CountryCodesListProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.langlist.Language;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.langlist.LanguageListProvidedAssetDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class AbstractProvidedAssetDTODeserializer extends JsonDeserializer<AbstractProvidedAssetDTO> {
    @Override
    public AbstractProvidedAssetDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);
        if(node.has("assets_file_version")){
            String version = node.get("assets_file_version").asText();
            if(node.has("initial_colors")){
                var initialColors = mapper.readValue(node.get("initial_colors").toString(), new TypeReference<HashMap<String, String>>(){});
                return AreaConfigurationProvidedAssetDTO.builder().assetsFileVersion(version).initialColors(initialColors).build();
            }
            if(node.has("countryCodes") && node.has("id")){
                var countryCodeDTOS = mapper.readValue(node.get("countryCodes").toString(), new TypeReference<List<CountryCodeDTO>>(){});
                var id = node.get("id").asText();
                return CountryCodesListProvidedAssetDTO.builder().assetsFileVersion(version).countryCodes(countryCodeDTOS).id(id).build();
            }
            if(node.has("languages") && node.has("id")){
                var id  = node.get("id").asLong();
                var languages = mapper.readValue(node.get("languages").toString(), new TypeReference<List<Language>>(){});
                return LanguageListProvidedAssetDTO.builder().assetsFileVersion(version).id(id).languages(languages).build();
            }
        }
        log.error("Could not deserialize AbstractProvidedAssetDTO" + node);
        return null;
    }
}
