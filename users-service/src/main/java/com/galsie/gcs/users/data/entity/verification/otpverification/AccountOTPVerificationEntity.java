package com.galsie.gcs.users.data.entity.verification.otpverification;


import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.data.entity.verification.OTPVerificationEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountOTPVerificationEntity extends OTPVerificationEntity {

    @ManyToOne
    @JoinColumn(name="user_id")
    UserAccountEntity user;
}
