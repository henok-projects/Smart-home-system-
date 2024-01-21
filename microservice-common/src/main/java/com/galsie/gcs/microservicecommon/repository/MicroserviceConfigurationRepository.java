package com.galsie.gcs.microservicecommon.repository;

import com.galsie.gcs.microservicecommon.data.entity.MicroserviceConfigurationEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MicroserviceConfigurationRepository extends GalRepository<MicroserviceConfigurationEntity, Long> {

    Optional<MicroserviceConfigurationEntity> findByMicroserviceAndConfigKey(String microservice, String configKey);
}
