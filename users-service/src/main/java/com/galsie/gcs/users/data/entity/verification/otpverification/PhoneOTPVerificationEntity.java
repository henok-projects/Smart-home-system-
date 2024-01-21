package com.galsie.gcs.users.data.entity.verification.otpverification;

import com.galsie.gcs.users.data.common.PhoneNumberHolder;
import com.galsie.gcs.users.data.entity.verification.OTPVerificationEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Child of {@link OTPVerificationEntity}
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneOTPVerificationEntity extends OTPVerificationEntity implements PhoneNumberHolder {

    @Column(name="country_code")
    private short countryCode;

    @Column(name="phone_number")
    private String phoneNumber;

}
