package com.galsie.gcs.homescommondata.repository.home.user;

import com.galsie.gcs.homescommondata.data.entity.home.user.AbstractHomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbstractHomeUserEntityRepository extends GalRepository<AbstractHomeUserEntity, Long> {

    Optional<AbstractHomeUserEntity> findByAppUserAppUserIdAndHomeHomeId(Long appUserId, Long homeId);

    List<AbstractHomeUserEntity> findAllByHome_HomeIdAndAppUser_AppUserIdNot(Long homeId, Long appUserIds);

}
