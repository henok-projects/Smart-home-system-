package com.galsie.gcs.users.data.entity.security;


import com.galsie.gcs.microservicecommon.lib.galsecurity.data.entity.GalSecurityCommonSessionEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserAuthSessionEntity extends GalSecurityCommonSessionEntity implements GalEntity<Long> {
    @Builder
    public UserAuthSessionEntity(Long id, UserAccountEntity user, String authToken,  String twoFAOTPVerificationToken, boolean expired, LocalDateTime validUntil, LocalDateTime lastAccess,
                                          LocalDateTime createdAt, LocalDateTime modifiedAt) {
        super(authToken, expired, validUntil, lastAccess, createdAt, modifiedAt);
        this.id = id;
        this.user = user;
        this.twoFAOTPVerificationToken = twoFAOTPVerificationToken;
    }
    public static long SESSION_VALIDITY_HOURS = 240; // 10 days

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserAccountEntity user;

    @Column(name="two_fa_otp_verification_token", nullable = true)
    String twoFAOTPVerificationToken; // if 2FA disabled OR when 2FA is done, would be nil (Authentication service updates this to nil when a session is authenticated and the token is valid)

    public boolean hasTwoFAOTPVerificationToken(){
        return this.getTwoFAOTPVerificationToken() != null;
    }
}
