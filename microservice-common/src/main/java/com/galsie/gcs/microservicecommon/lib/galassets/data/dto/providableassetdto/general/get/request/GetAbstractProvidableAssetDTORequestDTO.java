package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOType;

import java.util.Optional;

/**
 * An Interface implemented by to ProvidableDTORequest and MockProvidableDTORequest
 * enabling us to test a variety of situations via MockProvidableDTORequest keeping the
 * values returned by the methods consistent
 */

public interface GetAbstractProvidableAssetDTORequestDTO {
    boolean valid();

    Optional<AbstractProvidableAssetDTOType> getProvidableAssetDTOType();
}
