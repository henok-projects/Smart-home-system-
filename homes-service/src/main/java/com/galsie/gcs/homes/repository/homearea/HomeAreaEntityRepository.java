package com.galsie.gcs.homes.repository.homearea;

import com.galsie.gcs.homes.data.entity.home.area.HomeAreaEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeAreaEntityRepository extends GalRepository<HomeAreaEntity,Long> {

}
