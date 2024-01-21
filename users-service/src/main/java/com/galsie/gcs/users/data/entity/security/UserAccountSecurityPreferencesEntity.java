package com.galsie.gcs.users.data.entity.security;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserAccountSecurityPreferencesEntity implements GalEntity<Long> {
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId // the @MapsId annotation, which indicates that the primary key values will be copied from the User entity.
    @JoinColumn(name = "user_id") // must match the column name above sot that they are linked
    private UserAccountEntity user;

    @Column(name="hashed_app_pin", nullable = true)
    String hashedAppPin;

    @Column(name="is_2fa_enabled")
    private boolean is2FAEnabled;
}
