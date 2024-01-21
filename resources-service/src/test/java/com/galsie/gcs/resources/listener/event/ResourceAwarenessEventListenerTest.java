package com.galsie.gcs.resources.listener.event;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.subscription.request.MicroserviceProvidableAssetDTOsSubscriptionRequestDTO;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.events.SomeMicroserviceAwarenessLostEvent;
import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.resources.repository.sync.providableassetdtos.MicroserviceSubscribedProvidableAssetDTOEntityRepository;
import com.galsie.gcs.resources.service.providableassetdtos.MicroserviceProvidableAssetDTOsService;
import com.galsie.gcs.resources.service.providableassetdtos.MicroserviceProvidableAssetDTOsSubscriptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ResourceAwarenessEventListenerTest {

    @Autowired
    ProvidableAssetDTOsAwarenessLostHandler eventListener;

    @Autowired
    MicroserviceSubscribedProvidableAssetDTOEntityRepository microserviceSubscribedProvidableAssetDTOEntityRepository;

    @Autowired
    MicroserviceProvidableAssetDTOsService providableAssetDTOsService;

    @Autowired
    MicroserviceProvidableAssetDTOsSubscriptionService subscriptionService;

    @Test
    void handlePoolingEventCheckEntitiesAreDeleted() {
        //create subscription requests
        var providableAssetDTOTypes = (Set.of(ProvidableAssetDTOType.AREA_CONFIGURATION_CONTENT,ProvidableAssetDTOType.COUNTRY_CODE_MODEL));
        var service1 = GCSMicroservice.HOMES;
        var service2 = GCSMicroservice.CONTINUOUS_SERVICE;
        var uniqueInstanceId = "500";
        var version= "1.0.0";
        var request = MicroserviceProvidableAssetDTOsSubscriptionRequestDTO.builder().gcsMicroservice(service1).uniqueInstanceId(uniqueInstanceId)
                .uniqueInstanceId(uniqueInstanceId).providableAssetDTOTypeSet( providableAssetDTOTypes).version(version).build();
        var request2 = MicroserviceProvidableAssetDTOsSubscriptionRequestDTO.builder().gcsMicroservice(service2).uniqueInstanceId(uniqueInstanceId)
                .uniqueInstanceId(uniqueInstanceId).providableAssetDTOTypeSet( providableAssetDTOTypes).version(version).build();

        //subscribe and check that entities were persisted
        subscriptionService.receiveSubscriptionRequest(request);
        subscriptionService.receiveSubscriptionRequest(request2);
        var entities = microserviceSubscribedProvidableAssetDTOEntityRepository.findAllByMicroserviceAndUniqueInstanceId(service1, uniqueInstanceId);
        var entities2 = microserviceSubscribedProvidableAssetDTOEntityRepository.findAllByMicroserviceAndUniqueInstanceId(service2, uniqueInstanceId);
        Assertions.assertEquals(providableAssetDTOTypes.size(), entities.size());
        Assertions.assertEquals(providableAssetDTOTypes.size(), entities2.size());

        //Create mock event and send pass it to event listener
        var awarenessDTO = GCSMicroserviceAwarenessStatusDTO.builder().serviceName(service1).version(version).instanceId(uniqueInstanceId).lastPollTime(LocalDateTime.now().minusDays(8)).nextPollTime(LocalDateTime.now().minusDays(6)).build();
        eventListener.handlePoolingEvent(new SomeMicroserviceAwarenessLostEvent(null, null, List.of(awarenessDTO)));

        //Check that the right entities were deleted
        entities = microserviceSubscribedProvidableAssetDTOEntityRepository.findAllByMicroserviceAndUniqueInstanceId(service1, uniqueInstanceId);
        entities2 = microserviceSubscribedProvidableAssetDTOEntityRepository.findAllByMicroserviceAndUniqueInstanceId(service2, uniqueInstanceId);
        Assertions.assertEquals(0, entities.size());
        Assertions.assertEquals(providableAssetDTOTypes.size(), entities2.size());

        //remove persisted entities
        GCSResponse.removeEntities(microserviceSubscribedProvidableAssetDTOEntityRepository, entities2);
    }
}