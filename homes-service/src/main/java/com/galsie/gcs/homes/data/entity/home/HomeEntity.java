package com.galsie.gcs.homes.data.entity.home;

import com.galsie.gcs.homes.data.discrete.HomeStatus;
import com.galsie.gcs.homes.data.discrete.HomeType;
import com.galsie.gcs.homes.data.entity.home.address.HomeAddressEntity;
import com.galsie.gcs.homes.data.entity.home.area.HomeAreaEntity;
import com.galsie.gcs.homes.data.entity.home.fabric.HomeFabricInfoEntity;
import com.galsie.gcs.homes.data.entity.home.floor.HomeFloorEntity;
import com.galsie.gcs.homes.data.entity.home.invites.HomeInviteEntity;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.homescommondata.data.entity.home.AbstractHomeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeEntity extends AbstractHomeEntity implements GalEntity<Long> {

    @Column(name="name")
    String name;

    @OneToOne(mappedBy = "home",cascade = CascadeType.ALL)
    HomeAddressEntity address;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    HomeStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    HomeType type;

    @OneToOne(mappedBy = "home")
    HomeFabricInfoEntity fabricInfo;

    @OneToMany(mappedBy = "homeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    List<HomeInviteEntity> homeInviteEntities;

    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    List<HomeRoleEntity> homeRoleEntities;

    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    List<HomeFloorEntity> homeFloors;

    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    List<HomeAreaEntity> homeAreaEntities;

    @Column(name="created_at")
    LocalDateTime createdAt;

    @Column(name="modified_at")
    LocalDateTime modifiedAt;

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
