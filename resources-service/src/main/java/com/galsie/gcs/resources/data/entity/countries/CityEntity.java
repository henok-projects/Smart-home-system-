package com.galsie.gcs.resources.data.entity.countries;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "city")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CityEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="zone_id")
    ZoneEntity zone;

    @Column(name="name")
    String name;

    @Column(name="longitude")
    double longitude;

    @Column(name="latitude")
    double latitude;


}
