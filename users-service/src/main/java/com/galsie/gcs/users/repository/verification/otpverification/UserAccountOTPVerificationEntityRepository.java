package com.galsie.gcs.users.repository.verification.otpverification;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.AccountOTPVerificationEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountOTPVerificationEntityRepository extends GalRepository<AccountOTPVerificationEntity, Long> {

    Optional<AccountOTPVerificationEntity> findByUser(UserAccountEntity user);

}
