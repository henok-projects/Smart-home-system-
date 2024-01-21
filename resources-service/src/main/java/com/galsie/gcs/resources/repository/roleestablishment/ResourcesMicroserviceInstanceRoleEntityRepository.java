package com.galsie.gcs.resources.repository.roleestablishment;

import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceRole;
import com.galsie.gcs.resources.data.entity.roleestablishement.ResourcesMicroserviceInstanceRoleEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.util.Optional;

public interface ResourcesMicroserviceInstanceRoleEntityRepository extends GalRepository<ResourcesMicroserviceInstanceRoleEntity, Long> {

    Optional<ResourcesMicroserviceInstanceRoleEntity> findByRole(GCSResourceMicroserviceRole role);

}
