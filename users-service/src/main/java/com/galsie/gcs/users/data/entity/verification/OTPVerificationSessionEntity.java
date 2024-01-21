package com.galsie.gcs.users.data.entity.verification;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * An OTPVerificationSessionEntity represents a session
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OTPVerificationSessionEntity implements GalEntity<Long> {
    public static final int TOKEN_LENGTH = 128;
    public static final double CODE_LENGTH = 4;
    public static final int TOKEN_VALIDITY_MINUTES = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name="ver_entity_id")
    OTPVerificationEntity otpVerificationEntity;

    @Column(name="ver_token", unique = true)
    private String verificationToken;

    @Column(name="ver_code")
    private String hashedVerificationCode; // OTP

    // A token is verified if a request was made to perform otp verification with a matching OTP
    @Column(name="is_verified")
    private boolean isVerified;

    // Mainly, tokens are no longer active when validUntil is reached. They can be force expired though
    @Column(name="expired")
    private boolean expired;

    @Column(name="valid_until")
    private LocalDateTime validUntil;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="modified_at")
    private LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }

    /**
    Considered active if its not expired and the validUntil time hasn't been reached
     */
    public boolean isActive(){
        if (expired){
            return false;
        }
        return validUntil.toEpochSecond(ZoneOffset.MIN) - LocalDateTime.now().toEpochSecond(ZoneOffset.MIN) > 0;
    }
}
