package com.galsie.gcs.testservice.configuration;

import com.galsie.gcs.microservicecommon.lib.galassets.GCSGalAssetsConfiguration;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeSet;

@Component
public class TestGalAssetsConfiguration extends GCSGalAssetsConfiguration {

    @Override
    public TreeSet<ProvidableAssetDTOType> getSubscribableProvidedAssetDTOTypes() {
        return new TreeSet<>(List.of(ProvidableAssetDTOType.AREA_CONFIGURATION_CONTENT, ProvidableAssetDTOType.COUNTRY_CODE_MODEL));
    }

    @Override
    public TreeSet<ProvidableAssetDTOType> getStartUpProvidableAssetDTOTypes() {
        return new TreeSet<>(List.of(ProvidableAssetDTOType.AREA_CONFIGURATION_CONTENT));
    }

}
