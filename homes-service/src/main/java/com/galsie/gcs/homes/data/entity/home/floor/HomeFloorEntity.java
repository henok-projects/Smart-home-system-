package com.galsie.gcs.homes.data.entity.home.floor;

import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.area.HomeAreaEntity;
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
public class HomeFloorEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long uniqueId;

    @ManyToOne
    @JoinColumn(name="home_id")
    HomeEntity home;

    @OneToMany(fetch = FetchType.LAZY)
    List<HomeAreaEntity> areaEntity;

    @Column(name = "floor_number")
    Long floorNumber;

    @Column(name="floor_name")
    String floorName;

    @Column(name="created_at")
    LocalDateTime createdAt;

    @Column(name="modified_at")
    LocalDateTime modifiedAt;

    @PrePersist
    public void onCreate() {
    }

}
