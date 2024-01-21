package com.galsie.gcs.certauthority.repository;

import com.galsie.gcs.certauthority.data.discrete.RootCertificateAuthorityType;
import com.galsie.gcs.certauthority.data.entity.certificates.RCACEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RCACEntityRepository extends GalRepository<RCACEntity, Long> {



    Optional<RCACEntity> findByRootCertificateAuthorityType(RootCertificateAuthorityType rootCertificateAuthorityType);
}
