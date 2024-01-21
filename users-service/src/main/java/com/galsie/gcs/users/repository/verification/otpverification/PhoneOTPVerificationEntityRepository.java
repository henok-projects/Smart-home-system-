package com.galsie.gcs.users.repository.verification.otpverification;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.verification.otpverification.PhoneOTPVerificationEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneOTPVerificationEntityRepository extends GalRepository<PhoneOTPVerificationEntity, Long>{

    Optional<PhoneOTPVerificationEntity> findByCountryCodeAndPhoneNumber(short countryCode, String phoneNumber);

}
