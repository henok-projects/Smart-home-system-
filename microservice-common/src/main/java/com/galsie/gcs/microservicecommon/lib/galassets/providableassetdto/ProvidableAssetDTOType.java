package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.areaconfig.AreaConfigurationProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.countrycodes.CountryCodesListProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.langlist.LanguageListProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.ProvidedAssetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum ProvidableAssetDTOType implements AbstractProvidableAssetDTOType {

    AREA_CONFIGURATION_CONTENT(AreaConfigurationProvidedAssetDTO.class, AssetGroupType.APP_CONTENT, "area/area_config"),

    COUNTRY_CODE_MODEL(CountryCodesListProvidedAssetDTO.class, AssetGroupType.APP_CONTENT, "signup/countryCodesListModel"),

    LANGUAGE_LIST_MODEL(LanguageListProvidedAssetDTO.class, AssetGroupType.APP_CONTENT, "onboarding/languageListModel" );

    private final Class<? extends ProvidedAssetDTO> providedAssetDTOClassType;
    private final AssetGroupType assetGroupType;
    private final String pathRelativeToAssetGroupType;


    /**
     * method was created to be used by bootstrap for the sake of realising which ProvidableAssetDTOType was updated so
     * that it can send the update over to the subscribed microservices
     * @param path the path stored in the database
     * @return and Optional of the ProvidableAssetDTOType matching the path without the extension
     */
    public static Optional<ProvidableAssetDTOType> getAssetDTOTypeFromPath(String path) {
        String finalPath= path.split("\\.")[0];
        return Arrays.stream(ProvidableAssetDTOType.values())
                .filter(providableAssetDTOType -> providableAssetDTOType.getPathRelativeToAssetGroupType()
                        .equals(finalPath)).findFirst();
    }

    public static Optional<ProvidableAssetDTOType> getByName(String name) {
        return Arrays.stream(ProvidableAssetDTOType.values())
                .filter(providableAssetDTOType -> providableAssetDTOType.name().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public String getName() {
        return this.name();
    }
}
