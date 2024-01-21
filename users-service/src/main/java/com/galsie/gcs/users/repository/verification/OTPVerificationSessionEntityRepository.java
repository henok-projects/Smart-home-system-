package com.galsie.gcs.users.repository.verification;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.verification.OTPVerificationSessionEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPVerificationSessionEntityRepository extends GalRepository<OTPVerificationSessionEntity, Long> {

    Optional<OTPVerificationSessionEntity> findByVerificationToken(String token);
}
