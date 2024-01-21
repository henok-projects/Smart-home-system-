package com.galsie.gcs.homes.data.entity.home.device.group;


import com.galsie.gcs.homes.data.entity.home.device.HomeDeviceEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
// associated node to a group
public class HomeDeviceGroupAssocDeviceEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="uniqueId", nullable = false)
    Long uniqueId;

    @ManyToOne
    HomeDeviceGroupEntity group;

    @OneToOne
    @JoinColumn(name="home_device") // join column since HomeDeviceEntity don't have
    HomeDeviceEntity node;

    @Column(name="createdAt")
    LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }
}
