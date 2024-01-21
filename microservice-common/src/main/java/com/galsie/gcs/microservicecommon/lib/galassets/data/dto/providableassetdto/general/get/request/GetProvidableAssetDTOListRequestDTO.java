package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetProvidableAssetDTOListRequestDTO implements GetAbstractProvidableAssetDTOListRequestDTO<ProvidableAssetDTOType> {

    @NotNull
    Set<String>  providableAssetDTOTypes;

    public boolean valid() {
        return !getProvidableAssetDTOTypes().isEmpty();
    }

    public Set<ProvidableAssetDTOType> getProvidableAssetDTOTypes() {
        return providableAssetDTOTypes.stream().map(providableAssetDTOTypes -> ProvidableAssetDTOType.getByName(providableAssetDTOTypes).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }

}
