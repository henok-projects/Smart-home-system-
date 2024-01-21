package com.galsie.gcs.homes.data.entity.home.door;

import com.galsie.gcs.homes.data.entity.home.area.HomeAreaEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeDoorEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private HomeAreaEntity connectsFromArea;

    @Column(name = "connects_to")
    private Long connectsToArea;

    @Column(name = "door_name")
    private String doorName;

}
