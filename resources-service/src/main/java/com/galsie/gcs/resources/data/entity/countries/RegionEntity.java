package com.galsie.gcs.resources.data.entity.countries;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;


// asia, europe, ..
@Entity
@Table(name = "region")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
/**
 * A region has countries
 */
public class RegionEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="region_id", unique = true, nullable = false)
    private Long id;

    @Column(name="name", unique = true)
    String name;

    @OneToMany(mappedBy = "region")
    List<CountryEntity> countries;


}
