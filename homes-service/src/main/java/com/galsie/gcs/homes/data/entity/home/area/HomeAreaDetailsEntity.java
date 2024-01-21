package com.galsie.gcs.homes.data.entity.home.area;

import com.galsie.gcs.homes.data.discrete.HomeAreaType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class HomeAreaDetailsEntity implements GalEntity<Long> {

    @Id
    @Column(name = "home_id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @MapsId
    HomeAreaEntity homeAreaEntity;

    @Column(name="name", nullable = false)
    String name;

    @Column(name = "initials", columnDefinition = "CHAR(3)", nullable = false)
    private String initials;

    @Column(name = "color", nullable = false)
    private String initialsColor;

    @Column(name="type", nullable = false)
    HomeAreaType type;

}