package com.galsie.gcs.homes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.galassets.GCSGalAssetsProvidedDTOCacheService;
import com.galsie.gcs.microservicecommon.lib.galassets.GCSProvidableAssetDTOsConsumer;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOWithType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.countrycodes.CountryCodesListProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.langlist.LanguageListProvidedAssetDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
public class GCSProvidableAssetDTOsConsumerTests {

    @Autowired
    GCSProvidableAssetDTOsConsumer gcsGalAssetSubscribedListener;

    @Autowired
    GCSGalAssetsProvidedDTOCacheService gcsGalAssetsProvidedDTOCacheService;

    @Test
    void getSubscribedDTOFromListener() throws IOException {
        var cached = gcsGalAssetsProvidedDTOCacheService.getProvidableAssetDTOWithoutRequest(ProvidableAssetDTOType.COUNTRY_CODE_MODEL, CountryCodesListProvidedAssetDTO.class);
        Assertions.assertFalse(cached.isPresent());
        var countryCodesListModelDTO = new CountryCodesListProvidedAssetDTO("1", "0.0", new ArrayList<>(1));
        var abstractProvidableAssetWithTypeDTO = ProvidableAssetDTOWithType.builder().providedAssetDTO(countryCodesListModelDTO).providableAssetDTOType(ProvidableAssetDTOType.COUNTRY_CODE_MODEL).build();
        var mapper  = new ObjectMapper();
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        messageProperties.setContentEncoding("UTF-8");
        var body = mapper.writeValueAsString(abstractProvidableAssetWithTypeDTO).getBytes();
        var message = MessageBuilder.withBody(body).andProperties(messageProperties).build();
        gcsGalAssetSubscribedListener.consumeProvidableAssetQueueMessage(message);
        cached = gcsGalAssetsProvidedDTOCacheService.getProvidableAssetDTOWithoutRequest(ProvidableAssetDTOType.COUNTRY_CODE_MODEL, CountryCodesListProvidedAssetDTO.class);
        Assertions.assertTrue(cached.isPresent());
        gcsGalAssetsProvidedDTOCacheService.removeProvidedAssetDTO(ProvidableAssetDTOType.COUNTRY_CODE_MODEL);

    }

    @Test
    void getAssetNotSubscribedAndNotStartup(){
        var cached = gcsGalAssetsProvidedDTOCacheService.getProvidableAssetDTO(ProvidableAssetDTOType.LANGUAGE_LIST_MODEL, LanguageListProvidedAssetDTO.class);
        Assertions.assertFalse(cached.isPresent());
    }

    @Test
    void getSubscribedDTOFromCache(){
        var cached = gcsGalAssetsProvidedDTOCacheService.getProvidableAssetDTO(ProvidableAssetDTOType.COUNTRY_CODE_MODEL, CountryCodesListProvidedAssetDTO.class);
        Assertions.assertTrue(cached.isPresent());
        gcsGalAssetsProvidedDTOCacheService.removeProvidedAssetDTO(ProvidableAssetDTOType.COUNTRY_CODE_MODEL);
    }

}
