package com.galsie.gcs.resources.repository.countries;

import com.galsie.gcs.resources.data.entity.countries.RegionEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends GalRepository<RegionEntity, Long> {
    Optional<RegionEntity> findByName(String regionName);
}
