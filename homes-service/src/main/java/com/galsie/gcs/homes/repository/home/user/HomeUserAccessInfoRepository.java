package com.galsie.gcs.homes.repository.home.user;

import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserAccessInfoEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeUserAccessInfoRepository extends GalRepository<HomeUserAccessInfoEntity,Long> {

    List<HomeUserAccessInfoEntity> findByHomeUserEntity(HomeUserEntity homeUserEntity);

    Optional<HomeUserAccessInfoEntity>  findHomeUserAccessInfoEntityByHomeUserEntityAndHomeEntity(HomeUserEntity homeUserEntity, HomeEntity homeEntity);

}
