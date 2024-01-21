package com.galsie.gcs.gcssentry.repository.microservice;


import com.galsie.gcs.gcssentry.data.entity.microservice.GCSMicroserviceSessionEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.util.Optional;

public interface GCSMicroserviceSessionEntityRepository extends GalRepository<GCSMicroserviceSessionEntity, Long> {

    Optional<GCSMicroserviceSessionEntity> findBySessionToken(String sessionToken);

}
