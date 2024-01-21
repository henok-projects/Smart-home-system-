package com.galsie.gcs.resources.repository.sync.providableassetdtos;


import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.resources.data.entity.providableassetdtos.MicroserviceSubscribedProvidableAssetDTOEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MicroserviceSubscribedProvidableAssetDTOEntityRepository extends GalRepository<MicroserviceSubscribedProvidableAssetDTOEntity, Long> {

    List<MicroserviceSubscribedProvidableAssetDTOEntity> findAllBySubscribedProvidableAssetDTOType(ProvidableAssetDTOType dtoType);

    List<MicroserviceSubscribedProvidableAssetDTOEntity> findAllByMicroserviceAndUniqueInstanceId(GCSMicroservice microservice, String uniqueInstanceId);

    @Query("DELETE from MicroserviceSubscribedProvidableAssetDTOEntity e WHERE (e.microservice, e.uniqueInstanceId) IN :pairs")
    Long deleteAllByMicroserviceAndUniqueInstanceIdInPairs(@Param("pairs") Map<GCSMicroservice, String> pairs);

    void deleteByMicroserviceAndUniqueInstanceId(GCSMicroservice microservice, String uniqueInstanceId);

    List<MicroserviceSubscribedProvidableAssetDTOEntity> findAllBySubscribedProvidableAssetDTOTypeInAndMicroserviceAndUniqueInstanceId(Set<ProvidableAssetDTOType> dtoType, GCSMicroservice microservice, String uniqueInstanceId);

}
