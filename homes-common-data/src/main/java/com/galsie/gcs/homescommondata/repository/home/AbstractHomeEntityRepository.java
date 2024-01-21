package com.galsie.gcs.homescommondata.repository.home;

import com.galsie.gcs.homescommondata.data.entity.home.AbstractHomeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbstractHomeEntityRepository extends GalRepository<AbstractHomeEntity, Long> {
}

