package com.galsie.gcs.certauthority.repository.home.ica;

import com.galsie.gcs.certauthority.data.entity.certificates.home.ica.GCSHomeICACEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GCSHomeICACEntityRepository extends GalRepository<GCSHomeICACEntity, Long> {


    public Optional<GCSHomeICACEntity> findByHomeId(Long homeId);
}
