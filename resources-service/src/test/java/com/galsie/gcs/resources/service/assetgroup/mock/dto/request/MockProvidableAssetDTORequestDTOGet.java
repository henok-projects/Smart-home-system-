package com.galsie.gcs.resources.service.assetgroup.mock.dto.request;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request.GetAbstractProvidableAssetDTORequestDTO;
import com.galsie.gcs.resources.service.assetgroup.mock.discrete.MockProvidableAssetDTOType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MockProvidableAssetDTORequestDTOGet implements GetAbstractProvidableAssetDTORequestDTO {
    
    @NotNull
    private String providableAssetDTOTypeName;

    @Override
    public boolean valid() {
        return getProvidableAssetDTOType().isPresent();
    }
    
    @Override
    public Optional<AbstractProvidableAssetDTOType> getProvidableAssetDTOType(){
        return Arrays.stream(MockProvidableAssetDTOType.values())
                .filter(item -> item.name().equals(this.providableAssetDTOTypeName.toUpperCase(Locale.ROOT)))
                .map(type -> (AbstractProvidableAssetDTOType) type)
                .findFirst();
    }

}
