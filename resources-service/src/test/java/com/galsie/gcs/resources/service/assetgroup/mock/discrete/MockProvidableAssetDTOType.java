package com.galsie.gcs.resources.service.assetgroup.mock.discrete;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.ProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.areaconfig.AreaConfigurationProvidedAssetDTO;
import com.galsie.gcs.resources.service.assetgroup.mock.dto.BookAssetDTO;
import com.galsie.gcs.resources.service.assetgroup.mock.dto.PersonAssetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MockProvidableAssetDTOType implements AbstractProvidableAssetDTOType {

    BOOK_CONTENT(BookAssetDTO.class, AssetGroupType.APP_CONTENT, "area/test/book"),
    PERSON_CONTENT(PersonAssetDTO.class, AssetGroupType.APP_CONTENT,"area/test/person"),
    AREA_CONFIGURATION_BAD_PATH(AreaConfigurationProvidedAssetDTO.class, AssetGroupType.APP_CONTENT, "area/area_config2");

    private final Class<? extends ProvidedAssetDTO> providedAssetDTOClassType;
    private final AssetGroupType assetGroupType;
    private final String pathRelativeToAssetGroupType;

    @Override
    public String getName() {
        return this.name();
    }
}
