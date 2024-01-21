package com.galsie.gcs.homes.data.entity.home.user.preferences;

import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import org.springframework.lang.Nullable;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//	the keys and values are based on those defined in HomeUserNotificationPreferenceType
public class HomeUserNotificationPreferenceEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "preferences_id")
    HomeUserPreferencesEntity homeUserPreferences; // the preference that this is part of.

    @ManyToOne
    @JoinColumn(name = "home_user_id")
    private HomeUserEntity homeUser;

    @Column(name = "preference_key")
    private String preferenceKey;

    @Column(name = "preference_value")
    @Nullable
    private String preferenceValue;
}

