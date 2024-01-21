package com.galsie.gcs.resources.service.providableassetdtos;

import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.subscription.get.response.MicroserviceProvidableAssetDTOsSubscriptionResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.subscription.request.MicroserviceProvidableAssetDTOsSubscriptionRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.update.GCSUpdatedProvidedAssetDTOTypes;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import com.galsie.gcs.resources.data.entity.providableassetdtos.MicroserviceSubscribedProvidableAssetDTOEntity;
import com.galsie.gcs.resources.repository.sync.providableassetdtos.MicroserviceSubscribedProvidableAssetDTOEntityRepository;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.lib.utils.pair.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class MicroserviceProvidableAssetDTOsSubscriptionService {

    @Autowired
    MicroserviceSubscribedProvidableAssetDTOEntityRepository microserviceSubscribedProvidableAssetDTOEntityRepository;

    @Autowired
    GCSRabbitTemplate rabbitTemplate;

    @Transactional
    public GCSResponse<MicroserviceProvidableAssetDTOsSubscriptionResponseDTO> receiveSubscriptionRequest(MicroserviceProvidableAssetDTOsSubscriptionRequestDTO requestDTO){
        var providableAssetDTOTypeSet = requestDTO.getProvidableAssetDTOTypeSet();
        var currentSubs = microserviceSubscribedProvidableAssetDTOEntityRepository
                .findAllBySubscribedProvidableAssetDTOTypeInAndMicroserviceAndUniqueInstanceId(providableAssetDTOTypeSet,
                        requestDTO.getGcsMicroservice(), requestDTO.getUniqueInstanceId());
        GCSResponse.removeEntitiesThrows(microserviceSubscribedProvidableAssetDTOEntityRepository, currentSubs);
        var providableAssetDTOTypeEntityMap = getFilesForProvidableAssetDTOTypes(requestDTO.getProvidableAssetDTOTypeSet(),
                requestDTO.getGcsMicroservice(), requestDTO.getUniqueInstanceId(), requestDTO.getVersion());
        GCSResponse.saveEntitiesThrows(microserviceSubscribedProvidableAssetDTOEntityRepository, providableAssetDTOTypeEntityMap.values());
        var response = MicroserviceProvidableAssetDTOsSubscriptionResponseDTO.builder()
                .providableAssetDTOTypeList(providableAssetDTOTypeEntityMap.keySet()).build();
        return GCSResponse.response(response);
    }

    private Map<ProvidableAssetDTOType, MicroserviceSubscribedProvidableAssetDTOEntity> getFilesForProvidableAssetDTOTypes(Set<ProvidableAssetDTOType> types, GCSMicroservice microservice, String uniqueInstanceId,
                                                                                                                           String version) {
        Map<ProvidableAssetDTOType, MicroserviceSubscribedProvidableAssetDTOEntity> files = new HashMap<>();
        for (ProvidableAssetDTOType entry : types) {
            LoadedAssetGroup loadedAssetGroup;
            try {
                loadedAssetGroup = LoadedAssetGroup.sharedInstance(entry.getAssetGroupType());
            }catch (Exception e){
                log.warn("Failed to get loaded asset group for asset group type: " + entry.getAssetGroupType().name());
                continue;
            }
            var assetFile = loadedAssetGroup.getLoadedFile(entry.getPathRelativeToAssetGroupType());
            if (assetFile.isEmpty()) {
                continue;
            }
            var entity = MicroserviceSubscribedProvidableAssetDTOEntity.builder().subscribedProvidableAssetDTOType(entry)
            .microservice(microservice).uniqueInstanceId(uniqueInstanceId).version(version).build();
            files.put(entry, entity);
        }
        return files;
    }

    public void broadcastChangedProvidableAssetDTOs(GCSUpdatedProvidedAssetDTOTypes updatedProvidedAssetDTOTypes){
        rabbitTemplate.convertAndSend(GCSRabbitMQCommonQueueType.RESOURCE_PROVIDABLE_ASSETS, updatedProvidedAssetDTOTypes);
    }

    @Transactional
    public void deleteAllByMicroserviceAndUniqueInstanceIdInPairs(List<Pair<GCSMicroservice, String>> pairs) {
        var deletedCount = 0;
        for(Pair<GCSMicroservice, String> pair : pairs){
            deletedCount++;
            microserviceSubscribedProvidableAssetDTOEntityRepository.deleteByMicroserviceAndUniqueInstanceId(pair.getFirst(), pair.getSecond());
        }
        log.info("Deleted "+ deletedCount +" entities");
    }
}
