package com.galsie.gcs.resources.repository.countries;

import com.galsie.gcs.resources.data.entity.countries.CityEntity;
import com.galsie.gcs.resources.data.entity.countries.ZoneEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.util.List;


public interface CityRepository extends GalRepository<CityEntity, Long> {
    List<CityEntity> findAllByZone(ZoneEntity zone);

}
