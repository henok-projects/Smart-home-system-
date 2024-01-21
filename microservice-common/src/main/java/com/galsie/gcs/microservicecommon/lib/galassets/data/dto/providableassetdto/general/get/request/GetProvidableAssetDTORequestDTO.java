package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Arrays;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class GetProvidableAssetDTORequestDTO implements GetAbstractProvidableAssetDTORequestDTO {

    @NotNull
    private String providableAssetDTOType;


    @Override
    public boolean valid() {
        return ProvidableAssetDTOType.getByName(providableAssetDTOType).isPresent();
    }

    @Override
    public Optional<AbstractProvidableAssetDTOType> getProvidableAssetDTOType() {
        return Optional.ofNullable(ProvidableAssetDTOType.getByName(providableAssetDTOType).orElse(null));
    }


}
