package com.galsie.gcs.homes.data.entity.home.device.group;


import com.galsie.gcs.homes.data.discrete.HomeNodeGroupingType;
import com.galsie.gcs.homescommondata.data.entity.protocol.galsie.DiverseGroupTypeEntity;
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
public class HomeDeviceGroupEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id", nullable = false)
    Long uniqueId;

    @Column(name="name")
    String name;

    @Column(name="grouping_type")
    HomeNodeGroupingType groupingType;

    @ManyToOne
    @JoinColumn(name="type_id", nullable = true)// if the grouping type is diverse, this wouldn't be null
    DiverseGroupTypeEntity diverseNodeGroupType;

    @OneToMany(mappedBy = "group")
    List<HomeDeviceGroupAssocDeviceEntity> assocNodes;

    @Column(name="created_at")
    LocalDateTime createdAt;

    @Column(name="modified_At")
    LocalDateTime modifiedAt;


    protected void onCreateUpdate() throws Exception{
        if (groupingType == HomeNodeGroupingType.SINGLE_NODE_TYPE_GROUP){
            diverseNodeGroupType = null;
            return;
        }
        // diverse grouping
        if (diverseNodeGroupType == null){
            throw new Exception("Couldn't create/update HomeDeviceGroupEntity: Grouping type is " + HomeNodeGroupingType.DIVERSE_NODE_TYPE_GROUP.toString() + " but diverseNodeGroupType is not set");
        }

    }

    @PrePersist
    protected void onCreate() throws Exception {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
        this.onCreateUpdate();
    }
    @PreUpdate
    protected void onUpdate() throws Exception {
        modifiedAt = LocalDateTime.now();
        this.onCreateUpdate();
    }


}
