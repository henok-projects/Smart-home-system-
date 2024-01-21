package com.galsie.gcs.users.data.entity.verification;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.EmailOTPVerificationEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.PhoneOTPVerificationEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.AccountOTPVerificationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * <h3>General</h3>
 * An OTPVerificationEntity represents an entity for which OTPVerification may be requested. These can be:
 * - A EMAIL
 * - A PHONE
 * - A USER ACCOUNT (email or phone or other method)
 *
 * A One to Many relationship to {@link OTPVerificationSessionEntity} indicates that the same {@link OTPVerificationEntity} must be used for different OTPVerification requests
 * - This way, we avoid redundant data in our database, and make it convenient to get history of all otp verification requests.
 *
 * OTPVerificationEntity is a parent to {@link EmailOTPVerificationEntity], {@link PhoneOTPVerificationEntity} & {@link AccountOTPVerificationEntity }
 * --> Nothing prevents requesting raw verification for an email/phone that has a user account
 * --> Simply, its just not associated with the user account here, even if it actually has a user account
 * --> For verification associaed wit the user ccount, check {@link AccountOTPVerificationEntity }
 *
 *
 *  <h3>Inheritance Strategy</h3>
 *  Single table because we need to get the entities matching a token:
 *  - Using TABLE_PER_CLASS forces JPA to use UNION for that case, which can take more time & force more complex queries
 *  - Similar reasons for not using JOINED
 *
 *  Note that:
 *  - We highly care for efficiency here since the app may handle a large number of OTP requests
 *  - We would have used JOINED if there were more variance in the number of parameters used by the 2 entities
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class OTPVerificationEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "otpVerificationEntity")
    List<OTPVerificationSessionEntity> otpVerificationInfoEntities;
}
