package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.subscription.get.update;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GCSGalAssetSubscribedDTOUpdateDTO {

        @NotNull
        private String name;

        @NotNull
        private AbstractProvidedAssetDTO providedAssetDTO;

}
