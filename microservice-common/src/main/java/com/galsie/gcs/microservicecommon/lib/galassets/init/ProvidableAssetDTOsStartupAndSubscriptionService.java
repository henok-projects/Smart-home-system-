package com.galsie.gcs.microservicecommon.lib.galassets.init;

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceGeneralLocalProperties;
import com.galsie.gcs.microservicecommon.lib.galassets.GCSGalAssetsConfigurationDataProvider;
import com.galsie.gcs.microservicecommon.lib.galassets.GCSGalAssetsProvidedDTOCacheService;
import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.assetsubscription.GCSSubscriptionState;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request.GetProvidableAssetDTOListRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.response.GetProvidableAssetDTOListResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.subscription.get.response.MicroserviceProvidableAssetDTOsSubscriptionResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.events.ThisMicroserviceSubscriptionFailedEvent;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOWithTypeDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOWithType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.subscription.request.MicroserviceProvidableAssetDTOsSubscriptionRequestDTO;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.GCSRemoteRequests;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is used by {@link ProvidableAssetDTOsStartupAndSubscriptionHandler} to subscribe to DTOs and cache startup
 * DTOs
 */
@Slf4j
public class ProvidableAssetDTOsStartupAndSubscriptionService {

    @Autowired
    GCSGalAssetsConfigurationDataProvider gcsGalAssetsConfigurationDataProvider;

    @Autowired
    GCSGalAssetsProvidedDTOCacheService assetManager;

    @Autowired
    GCSMicroserviceGeneralLocalProperties gcsMicroserviceGeneralLocalProperties;

    @Autowired
    GCSRemoteRequests gcsRemoteRequests;

    @Autowired
    GCSEventManager eventManager;

    @Getter
    @Setter
    private static GCSSubscriptionState subscriptionState = GCSSubscriptionState.NOT_SUBSCRIBED;

    public void requestSubscribableToSubscribableDTOs() {
        var subscribableProvidedAssetDTOType = gcsGalAssetsConfigurationDataProvider.getSubscribableProvidedAssetDTOTypes();
        var gcsMicroserviceOpt = gcsMicroserviceGeneralLocalProperties.getGCSMicroservice();
        if(gcsMicroserviceOpt.isEmpty()){
            log.error("No valid GCSMicroservice found in GCSMicroserviceGeneralLocalProperties");
            return;
        }
        var request = MicroserviceProvidableAssetDTOsSubscriptionRequestDTO.builder().
                gcsMicroservice(gcsMicroserviceOpt.get())
                .uniqueInstanceId(gcsMicroserviceGeneralLocalProperties.getInstanceUniqueId())
                .version(gcsMicroserviceGeneralLocalProperties.getVersion())
                .providableAssetDTOTypeSet(subscribableProvidedAssetDTOType).build();
        try {
            var response = gcsRemoteRequests.initiateRequest(MicroserviceProvidableAssetDTOsSubscriptionResponseDTO.class)
                    .destination(GCSMicroservice.RESOURCES, "/providableAssetDTOs", "/subscribe")
                    .httpMethod(HttpMethod.POST)
                    .setRequestPayload(request)
                    .performRequestWithGCSResponse()
                    .toGCSResponse();
            if (response.hasError()) {
                var error = response.getGcsError();
                log.error("Failed to subscribe to DTOs : " + error.getErrorType().name() + " " + error.getErrorMsg());
                eventManager.callEvent(new ThisMicroserviceSubscriptionFailedEvent());
                return;
            }
        }catch (Exception e){
            log.error("Failed to subscribe to DTOs : " + e.getMessage());
            eventManager.callEvent(new ThisMicroserviceSubscriptionFailedEvent());
            return;
        }
        subscriptionState = GCSSubscriptionState.SUBSCRIBED;
    }

    public void requestLoadStartupProvidableAssetDTOs() {
        if(subscriptionState == GCSSubscriptionState.NOT_SUBSCRIBED){
            log.warn("Failed to get startup dtos becuse the subscipton state is " + subscriptionState.name());
            return;
        }
        var subscribableProvidedAssetDTOType = gcsGalAssetsConfigurationDataProvider.getSubscribableProvidedAssetDTOTypes();
        var startUpProvidableAssetDTOs = gcsGalAssetsConfigurationDataProvider.getStartUpProvidableAssetDTOTypes();
        var discrepancy = areStartupDTOsSubscribedTo(startUpProvidableAssetDTOs, subscribableProvidedAssetDTOType);
        if(discrepancy) {
            log.warn("Failed to get startup dtos: there exists a dto in startup that's not in the subscribable dto list." +
                    " ALL startup dtos must be subscribed dto because otherwise the cached startup dto couldn't be updated");
            return;
        }
        try {
            requestAndCacheProvidableAssetDTOs(startUpProvidableAssetDTOs);
        } catch (Exception e) {
            log.error("Failed to get startup dtos : " + e.getMessage());
        }
    }

    public void requestAndCacheProvidableAssetDTOs(Set<ProvidableAssetDTOType> providableAssetDTOTypeSet) {
        var request = GetProvidableAssetDTOListRequestDTO.builder()
                .providableAssetDTOTypes(providableAssetDTOTypeSet.stream().map(ProvidableAssetDTOType::getName)
                        .collect(Collectors.toSet())).build();
        try {
            var gcsResponse = gcsRemoteRequests.initiateRequest(GetProvidableAssetDTOListResponseDTO.class)
                    .destination(GCSMicroservice.RESOURCES, "/providableAssetDTOs", "/request/dtos")
                    .httpMethod(HttpMethod.POST)
                    .setRequestPayload(request)
                    .performRequestWithGCSResponse()
                    .toGCSResponse();
            if (gcsResponse.hasError()) {
                var error = gcsResponse.getGcsError();
                log.error("Failed to get startupDTOS : " + error.getErrorType().name() + " " + error.getErrorMsg());
                return;
            }
            var responseProvidedAssetDTOs = gcsResponse.getResponseData().getProvidedAssetDTOs();
            if (responseProvidedAssetDTOs == null || responseProvidedAssetDTOs.isEmpty()) {
                log.error("Failed to get startupDTOS : responseProvidedAssetDTOs is null or empty");
                return;
            }
            List<AbstractProvidableAssetDTOWithTypeDTO> abstractProvidableAssetDTOWithTypeDTOS = new ArrayList<>();
            var listVersion = providableAssetDTOTypeSet.stream().toList();
            for (int i = 0; i < providableAssetDTOTypeSet.size(); i++) {
                var providableAssetDTOType = listVersion.get(i);
                var entry = responseProvidedAssetDTOs.get(i);
                if (entry != null) {
                    var updateCacheDTO = ProvidableAssetDTOWithType.builder().providableAssetDTOType(providableAssetDTOType)
                            .providedAssetDTO(entry).build();
                    abstractProvidableAssetDTOWithTypeDTOS.add(updateCacheDTO);
                }
            }
            assetManager.receiveProvidedAssetDTOUpdate(true, abstractProvidableAssetDTOWithTypeDTOS);//startup dtos are cached when loaded since they are subscribed to
        }catch (Exception e){
            log.error("Failed to get startupDTOS : " + e.getMessage());
        }
    }

    /**
     * Checks if all the startup DTOs are subscribable
     * @param startupDTOs
     * @param subscribedDTOs
     * @return
     */
    private boolean areStartupDTOsSubscribedTo(Set<ProvidableAssetDTOType> startupDTOs, Set<ProvidableAssetDTOType> subscribedDTOs){
        if(startupDTOs.size() > subscribedDTOs.size()){
            return true;
        }
       for(ProvidableAssetDTOType dtoType: startupDTOs){
           String name = dtoType.name();
           if(subscribedDTOs.stream().noneMatch(s->s.getName().equals(name))){
               return true;
           }
       }
       return false;
    }

}
