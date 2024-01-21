package com.galsie.gcs.users.data.entity;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.users.data.discrete.UserAccountSecurityType;
import com.galsie.gcs.users.data.discrete.UserAccountStatus;
import com.galsie.gcs.users.data.entity.security.UserAccountSecurityPreferencesEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserAccountEntity implements GalEntity<Long> {
    public static int USERNAME_LENGTH = 30;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;


    @Column(name="username", unique = true)
    private String username;

    @Column(name="security_type", nullable = false)
    private UserAccountSecurityType securityType;

    @Column(name="account_status", nullable = false)
    private UserAccountStatus accountStatus;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserAccountSecurityPreferencesEntity securityPreferences;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserInfoEntity userInfo;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserEmailEntity userEmail = null;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserProfilePhotoEntity userProfilePhoto = null;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserPhoneEntity userPhone = null;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="modified_at")
    private  LocalDateTime modifiedAt;

//    @Override
//    public UserAccountDTO toDTO() {
//        return new UserAccountDTO(id, username, securityType, accountStatus, isVerified(), userInfo.toDTO(), createdAt, modifiedAt);
//    }
//
//    public boolean isVerified(){
//        return (userEmail != null && userEmail.isVerified()) || (userPhone != null && userPhone.isVerified());
//    }
}
