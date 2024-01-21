package com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster;

import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.mtr.PhysicalMTRDeviceClusterEntity;
import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.HomeDeviceDataEntity;
import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.HomeDeviceMTRClusterAttributeDataEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Holds the data for native Matter Clusters
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeDeviceMTRClusterDataEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;

    /**
     * The cluster (which is part of an endpoint) that this entity is for
     *
     * NOTE:
     * - this is many to one SINCE the device may be commissioned onto multiple homes
     * - WE SHOULD NOT HAVE 2 instances of HomeDeviceMTRClusterDataEntity for the same home
     */
    @ManyToOne
    PhysicalMTRDeviceClusterEntity physicalMTRDeviceClusterEntity;

    @ManyToOne
    HomeDeviceDataEntity deviceData; // The parent that carries all the cluster data

    @OneToMany(mappedBy = "homeDeviceClusterDataEntity")
    List<HomeDeviceMTRClusterAttributeDataEntity> homeDeviceMTRClusterAttributeDataEntity;

}
