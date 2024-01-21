package com.galsie.gcs.homes.repository.homearea.details;

import com.galsie.gcs.homes.data.entity.home.area.HomeAreaDetailsEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;

import java.util.List;

public interface HomeAreaDetailsRepository extends GalRepository<HomeAreaDetailsEntity,Long> {

    List<HomeAreaDetailsEntity> findAllByName(String name);
}
