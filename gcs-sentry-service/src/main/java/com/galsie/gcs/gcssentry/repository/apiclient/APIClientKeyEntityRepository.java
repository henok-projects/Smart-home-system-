package com.galsie.gcs.gcssentry.repository.apiclient;

import com.galsie.gcs.gcssentry.data.entity.apiclient.APIClientKeyEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.util.Optional;

public interface APIClientKeyEntityRepository extends GalRepository<APIClientKeyEntity, Long> {

    Optional<APIClientKeyEntity> findBySessionToken(String sessionToken);

}
