package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.ProvidedAssetDTO;

/**
 * An Interface implemented by to {@link ProvidableAssetDTOType} and MockProvidableDTOAssetType
 * enabling us to test a variety of situations via MockProvidableDTOAssetType without
 * tampering with production code
 */

public interface AbstractProvidableAssetDTOType {
    Class<? extends ProvidedAssetDTO> getProvidedAssetDTOClassType();
    AssetGroupType getAssetGroupType();
    String getPathRelativeToAssetGroupType();
    String getName();

}
