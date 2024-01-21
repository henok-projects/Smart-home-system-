package com.galsie.gcs.microservicecommon.lib.galsecurity.data.entity;

import com.galsie.lib.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GalSecurityCommonSessionEntity {
    public static long AUTH_TOKEN_LENGTH = 128; // 128 chars
    public static long SESSION_VALIDITY_HOURS = 240; // 10 days

    @Column(name="sessionToken")
    String sessionToken;

    /*
    NOTE:
    - When a session is force expired, it can't be accessed irrespective of the validUntilTime
    - Checking is a session is not expired is not enough, if this is false, the session may still be invalid. The method to use is isValid()
     */
    @Column(name="force_expired")
    boolean forceExpired; //

    @Column(name="valid_until")
    LocalDateTime validUntil;

    @Column(name="last_access")
    LocalDateTime lastAccess; // last time user accessed through this token. session expires when session isn't accessed for UserAuthenticationService#maximumSessionInactiveTimeSeconds

    @Column(name="created_at")
    LocalDateTime createdAt;

    @Column(name="modified_at")
    LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }

    public boolean isExpired(){
        if (isForceExpired()){
            return false;
        }

        return DateUtils.secondsBetween(LocalDateTime.now(), validUntil) > 0;
    }

    public long getRemainingTimeUntilValidityEndsInMinutes(){
        return DateUtils.secondsBetween(LocalDateTime.now(), validUntil) / 60;
    }


    public void updateLastAccess() {
        this.lastAccess = LocalDateTime.now();
    }



}
