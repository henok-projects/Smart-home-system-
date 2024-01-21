package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.subscription.request;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
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
public class MicroserviceProvidableAssetDTOsSubscriptionRequestDTO {

    @NotNull
    private GCSMicroservice gcsMicroservice;


    /**
     * This is the version of the microservice that is subscribing to the DTOs but is not used as any form of validation
     */
    @NotNull
    private String version;

    @NotNull
    private String uniqueInstanceId;


    /**
     * This is a set of all the ProvidableAssetDTOTypes that are being subscribed to
     */
    @NotNull
    private Set<ProvidableAssetDTOType> providableAssetDTOTypeSet;

}
