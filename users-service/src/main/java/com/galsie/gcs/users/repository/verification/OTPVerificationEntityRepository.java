package com.galsie.gcs.users.repository.verification;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.verification.OTPVerificationEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPVerificationEntityRepository extends GalRepository<OTPVerificationEntity, Long> {
}
