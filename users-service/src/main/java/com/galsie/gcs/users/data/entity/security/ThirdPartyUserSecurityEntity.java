package com.galsie.gcs.users.data.entity.security;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.users.data.discrete.OAuthAppType;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ThirdPartyUserSecurityEntity implements GalEntity<Long> {
    // For UserAccountSecurityType.GALSIE
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // foreign key would be the same as the user id. The column name doesn't matter
    private Long id;

    @OneToOne
    @MapsId
    private UserAccountEntity user;

    @Column(nullable = false)
    private OAuthAppType appType;

    // TODO: Figure out haha.

    @Column(name="created_at", updatable = false)
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
}
