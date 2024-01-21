package com.galsie.gcs.resources.controller.providabaleassetdtos;

import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request.GetProvidableAssetDTOListRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request.GetProvidableAssetDTORequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.subscription.request.MicroserviceProvidableAssetDTOsSubscriptionRequestDTO;
import com.galsie.gcs.resources.service.providableassetdtos.MicroserviceProvidableAssetDTOsService;
import com.galsie.gcs.resources.service.providableassetdtos.MicroserviceProvidableAssetDTOsSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/providableAssetDTOs")
public class ProvidableAssetDTOsController {

    @Autowired
    MicroserviceProvidableAssetDTOsService providableAssetDTOsService;

    @Autowired
    MicroserviceProvidableAssetDTOsSubscriptionService subscriptionService;

    @PostMapping("/request/dto")
    public ResponseEntity<?> getDTO(@RequestBody GetProvidableAssetDTORequestDTO providableAssetDTORequestDTO) {
        return providableAssetDTOsService.getLoadedAssetDTO(providableAssetDTORequestDTO).toResponseEntity();
    }

    @PostMapping("/request/dtos")
    public ResponseEntity<?> getDTOs(@RequestBody GetProvidableAssetDTOListRequestDTO providableAssetDTOListRequestDTO){
        return providableAssetDTOsService.getLoadedAssetDTOs(providableAssetDTOListRequestDTO).toResponseEntity();
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody MicroserviceProvidableAssetDTOsSubscriptionRequestDTO galAssetsProvidableDTOListSubscriptionRequestDTO){
        return subscriptionService.receiveSubscriptionRequest(galAssetsProvidableDTOListSubscriptionRequestDTO).toResponseEntity();
    }

}
