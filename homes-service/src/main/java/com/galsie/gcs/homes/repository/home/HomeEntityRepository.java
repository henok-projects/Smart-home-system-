package com.galsie.gcs.homes.repository.home;

import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeEntityRepository extends GalRepository<HomeEntity, Long> {

    Optional<HomeEntity> findByHomeId(Long homeId);

}
