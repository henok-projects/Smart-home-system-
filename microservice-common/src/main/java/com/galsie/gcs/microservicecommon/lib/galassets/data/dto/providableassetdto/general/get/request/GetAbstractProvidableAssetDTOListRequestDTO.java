package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOType;

import java.util.Set;

public interface GetAbstractProvidableAssetDTOListRequestDTO<T extends AbstractProvidableAssetDTOType> {

    boolean valid();

    Set<T> getProvidableAssetDTOTypes();

}
