package com.galsie.gcs.homes.repository.home.user;

import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HomeUserEntityRepository extends GalRepository<HomeUserEntity,Long> {
}