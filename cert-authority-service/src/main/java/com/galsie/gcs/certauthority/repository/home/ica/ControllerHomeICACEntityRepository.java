package com.galsie.gcs.certauthority.repository.home.ica;

import com.galsie.gcs.certauthority.data.entity.certificates.home.ica.ControllerHomeICACEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControllerHomeICACEntityRepository extends GalRepository<ControllerHomeICACEntity, Long> {

}
