package com.galsie.gcs.users.repository.verification.otpverification;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.verification.otpverification.EmailOTPVerificationEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailOTPVerificationEntityRepository extends GalRepository<EmailOTPVerificationEntity, Long> {

    public Optional<EmailOTPVerificationEntity> findByEmail(String email);
}
