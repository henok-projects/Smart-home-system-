package com.galsie.gcs.resources.data.entity.countries;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "country")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CountryEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="country_id", nullable = false)
    Long id;

    @Column(name="name")
    String name;

    @Column(name="iso2_Code")
    String iso2Code;

    @Column(name="iso3_Code")
    String iso3Code;

    @OneToMany(mappedBy = "country")
    List<PhoneCodeEntity> phoneCodes;

    @ManyToOne
    @JoinColumn(name="region_id")
    RegionEntity region;

    @OneToMany(mappedBy = "country", fetch = FetchType.EAGER)
    List<ZoneEntity> zones;

    @Column(name="zonesLabel")
    String zonesLabel; // State/Province/....  TODO: Update whatever needs to beupdated to account for

    @Column(columnDefinition = "VARCHAR(255) DEFAULT '^[\\p{L}\\p{N}]{2,10}$'")
    private String zipcodePattern;


}
