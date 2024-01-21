package com.galsie.gcs.microservicecommon.lib.galassets;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOWithTypeDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.init.*;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.update.GCSUpdatedProvidedAssetDTOTypes;
import com.galsie.gcs.microservicecommon.lib.gcsjackson.GCSObjectMapper;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.GCSRabbitCommonQueueConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashSet;

/**
 * When are resource has been updated or created, the resource service with a manager role will broadcast
 */
@Slf4j
public class GCSProvidableAssetDTOsConsumer {

    @Autowired(required = false)
    GCSGalAssetsProvidedDTOCacheService gcsGalAssetsProvidedDTOCacheService;

    @Autowired
    ProvidableAssetDTOsStartupAndSubscriptionService gcsStartupSubscriptionService;

    @Autowired
    GCSObjectMapper mapper;

    @Autowired
    GCSGalAssetsConfigurationDataProvider gcsGalAssetsConfigurationDataProvider;


    @GCSRabbitCommonQueueConsumer(types = GCSRabbitMQCommonQueueType.RESOURCE_PROVIDABLE_ASSETS)
    public void consumeProvidableAssetQueueMessage(Message message) {
        log.info("ProvidableAssetQueue message received");
        var subscribedProvidableAssetDTOTypeList = gcsGalAssetsConfigurationDataProvider.getSubscribableProvidedAssetDTOTypes();
        try{
            var abstractProvidableAssetWithTypeDTOs = mapper.readValueFromMessage(message, AbstractProvidableAssetDTOWithTypeDTO.class);
            if(subscribedProvidableAssetDTOTypeList.contains(abstractProvidableAssetWithTypeDTOs.getProvidableAssetDTOType())){
                gcsGalAssetsProvidedDTOCacheService.receiveProvidedAssetDTOUpdate(true, abstractProvidableAssetWithTypeDTOs);
            }
        }catch (IOException exception){
            log.error("There was problem deserializing the message: " + exception.getMessage());
        }
    }

    /**
     * This method is used to consume the updatedProvidedAssetDTOTypes broadcast from resources service, it will check if the set
     * in the broadcast is currently cached and if so it will request the DTOs from resources service and cache the updated version
     * @param message
     */
    @GCSRabbitCommonQueueConsumer(types = GCSRabbitMQCommonQueueType.RESOURCE_NOTIFICATION)
    public void consumeUpdatedProvidableAssetDTOTypesBroadcast(Message message) {
        log.info("Message heard "+ message);
        var subscribedProvidableAssetDTOTypeList = gcsGalAssetsConfigurationDataProvider.getSubscribableProvidedAssetDTOTypes();
        try{
            var gcsUpdatedProvidedAssetDTOTypes = mapper.readValueFromMessage(message, GCSUpdatedProvidedAssetDTOTypes.class);
            var validUpdatedAssetDTOTypes = new HashSet<ProvidableAssetDTOType>();
            for(var providableAssetDTOType: gcsUpdatedProvidedAssetDTOTypes.getUpdatedProvidedAssetDTOTypes()){
                if(subscribedProvidableAssetDTOTypeList.contains(providableAssetDTOType)){
                    var providedAssetDTO = gcsGalAssetsProvidedDTOCacheService.getProvidableAssetDTO(providableAssetDTOType, providableAssetDTOType.getProvidedAssetDTOClassType());
                    if(providedAssetDTO.isPresent()){
                        validUpdatedAssetDTOTypes.add(providableAssetDTOType);
                    }
                }
            }
            if(!validUpdatedAssetDTOTypes.isEmpty()){
                gcsStartupSubscriptionService.requestAndCacheProvidableAssetDTOs(validUpdatedAssetDTOTypes);
            }
        }catch (Exception exception){
            log.info("Message not consumed");
        }
    }

}
