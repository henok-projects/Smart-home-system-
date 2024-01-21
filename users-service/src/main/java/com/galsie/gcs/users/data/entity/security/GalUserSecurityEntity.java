package com.galsie.gcs.users.data.entity.security;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class GalUserSecurityEntity implements GalEntity<Long> {
    // For UserAccountSecurityType.GALSIE
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId // the @MapsId annotation, which indicates that the primary key values will be copied from the User entity.
    @JoinColumn(name = "user_id") // must match the column name above sot that they are linked
    private UserAccountEntity user;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "modified_at")
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
