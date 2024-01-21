package com.galsie.gcs.homes.data.entity.home.area;

import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.device.HomeDeviceEntity;
import com.galsie.gcs.homes.data.entity.home.door.HomeDoorEntity;
import com.galsie.gcs.homes.data.entity.home.floor.HomeFloorEntity;
import com.galsie.gcs.homes.data.entity.home.windows.HomeWindowEntity;
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
public class HomeAreaEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name="home_id")
    HomeEntity home;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "homeAreaEntity")
    HomeAreaDetailsEntity areaDetailsEntity;

    @OneToMany(mappedBy = "connectsFromArea", cascade = CascadeType.ALL, orphanRemoval = true)
    List<HomeDoorEntity> doors;

    @OneToMany(mappedBy = "homeAreaEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    List<HomeWindowEntity> windows;

    @ManyToOne
    HomeFloorEntity floorOfArea;

    @OneToMany(mappedBy = "area")
    List<HomeDeviceEntity> nodes; // Note: Groups of an area determined by loading all home groups which contain any area node.

}