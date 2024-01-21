package com.galsie.gcs.homescommondata.repository.appuser;

import com.galsie.gcs.homescommondata.data.entity.user.AppUserRemoteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRemoteEntityRepository extends GalRepository<AppUserRemoteEntity, Long> {

}
