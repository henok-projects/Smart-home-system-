package com.galsie.gcs.gcssentry.repository.microservice;

import com.galsie.gcs.gcssentry.data.entity.microservice.GCSMicroserviceSecurityEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.util.List;

public interface GCSMicroserviceSecurityEntityRepository extends GalRepository<GCSMicroserviceSecurityEntity, Long> {

    List<GCSMicroserviceSecurityEntity> findAllByHashedPwd(String hashedPwd);

}
