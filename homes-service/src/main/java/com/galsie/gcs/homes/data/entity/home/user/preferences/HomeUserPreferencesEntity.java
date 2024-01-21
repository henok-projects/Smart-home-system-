package com.galsie.gcs.homes.data.entity.home.user.preferences;


import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//this entity is the root entity which all preference keys of various categories are mapped to.
public class HomeUserPreferencesEntity implements GalEntity<Long> {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    HomeUserEntity homeUser;

    // Each preference entity holds a key and a value.
    @OneToMany(mappedBy = "homeUserPreferences")
    List<HomeUserNotificationPreferenceEntity> homeUserNotificationPreferenceEntities;
}
