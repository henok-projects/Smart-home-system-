package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.subscription.get.response;

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
public class MicroserviceProvidableAssetDTOsSubscriptionResponseDTO {

    //This is a set of all the providable asset types that were successfully subscribed to
    @NotNull
    Set<ProvidableAssetDTOType> providableAssetDTOTypeList;

}
