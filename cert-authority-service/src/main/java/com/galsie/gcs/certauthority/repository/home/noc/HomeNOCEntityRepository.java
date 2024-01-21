package com.galsie.gcs.certauthority.repository.home.noc;

import com.galsie.gcs.certauthority.data.entity.certificates.home.noc.HomeNOCEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeNOCEntityRepository extends GalRepository<HomeNOCEntity, Long> {
}
