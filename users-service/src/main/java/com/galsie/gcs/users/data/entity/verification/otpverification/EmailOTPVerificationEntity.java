package com.galsie.gcs.users.data.entity.verification.otpverification;

import com.galsie.gcs.users.data.entity.verification.OTPVerificationEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Raw in the sense that this entity is not associated to the user account, even if the email is actually for a user account
 * - Check out {@link OTPVerificationEntity}
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailOTPVerificationEntity extends OTPVerificationEntity {
    // Email should be unique
    @Column(name="email", unique = true)
    String email;

}
