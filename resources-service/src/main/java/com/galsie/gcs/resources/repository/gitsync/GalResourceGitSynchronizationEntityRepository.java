package com.galsie.gcs.resources.repository.gitsync;

import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.resources.data.entity.gitsync.GalResourceGitSynchronizationEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.util.Optional;

public interface GalResourceGitSynchronizationEntityRepository extends GalRepository<GalResourceGitSynchronizationEntity, Long> {

    Optional<GalResourceGitSynchronizationEntity>  findByGcsMicroserviceAndInstanceId(GCSMicroservice gcsMicroservice, String instanceId);

    Optional<GalResourceGitSynchronizationEntity> findByCurrentCommitSHA(String currentCommitSHA);

}
