package com.galsie.gcs.users.data.entity;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.users.data.common.PhoneNumberHolder;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserPhoneEntity implements PhoneNumberHolder, GalEntity<Long> {
    @Id
    @Column(name = "user_id") // foreign key would be the same as the user id. The column name doesn't matter
    private Long id;

    @OneToOne(cascade=CascadeType.ALL)
    @MapsId // the @MapsId annotation, which indicates that the primary key values will be copied from the User entity.
    @JoinColumn(name = "user_id")
    private UserAccountEntity user;

    @Column(name="country_code")
    private short countryCode;

    @Column(name="phone_number")
    private String phoneNumber;

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

}
