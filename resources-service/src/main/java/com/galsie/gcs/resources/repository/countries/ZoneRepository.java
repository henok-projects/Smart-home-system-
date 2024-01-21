package com.galsie.gcs.resources.repository.countries;

import com.galsie.gcs.resources.data.entity.countries.CountryEntity;
import com.galsie.gcs.resources.data.entity.countries.ZoneEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.util.List;

public interface ZoneRepository extends GalRepository<ZoneEntity, Long> {

    List<ZoneEntity> findAllByCountry(CountryEntity countryEntity);

}
