package com.galsie.gcs.homescommondata.data.entity.serviceintermediate;

import com.galsie.gcs.homescommondata.data.discrete.HomeDeviceStatus;
import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.PhysicalDeviceRemoteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Setter
@Getter
public abstract class AbstractHomeDeviceEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unique_id", nullable = false)
    private Long uniqueId;

    @ManyToOne
    PhysicalDeviceRemoteEntity baseDevice;

    @Column(name="status")
    private HomeDeviceStatus status; // this.onCreate and this.onModify will set automatically to decommissioned if this.decommissionedAt is set


}

