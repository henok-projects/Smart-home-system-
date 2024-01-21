package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.update;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Set;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GCSUpdatedProvidedAssetDTOTypes {

    @NotNull
    Set<ProvidableAssetDTOType> updatedProvidedAssetDTOTypes;

}
