package com.galsie.gcs.resources.data.entity.countries;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "zone")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
/**
 * A zone has cities. Depending on the country, the zone might be: State/Province/
 */
public class ZoneEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="country_id")
    CountryEntity country;

    @Column(name="zone_code")
    String zoneCode;

    @Column(name="name")
    String name;

    @OneToMany(mappedBy = "zone", fetch = FetchType.EAGER)
    List<CityEntity> cities;


}
