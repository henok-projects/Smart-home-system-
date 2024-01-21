package com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute;

import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.mtr.PhysicalMTRDeviceClusterAttributeEntity;
import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.HomeDeviceMTRClusterDataEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.lib.utils.lang.NotNull;

import javax.persistence.*;
import java.util.List;


@Entity
public class HomeDeviceMTRClusterAttributeDataEntity implements GalEntity<Long> {


    @ManyToOne(optional = false)
    HomeDeviceMTRClusterDataEntity homeDeviceMTRClusterDataEntity;

    /**
     * The attribute (which is part of a cluster in an endpoint) that this entity is for
     *
     * NOTE:
     * - this is many to one SINCE the device may be commissioned onto multiple homes (of-course sequentially & not in parallel)
     * - WE SHOULD NOT HAVE 2 instances of HomeDeviceMTRClusterAttributeDataEntity for the same home
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name="phys_device_cluster_attr", nullable = false)
    PhysicalMTRDeviceClusterAttributeEntity physicalMTRDeviceClusterAttributeEntity;

    @OneToMany(mappedBy = "mtrClusterDataEntity")
    List<HomeDeviceMTRClusterAttributeDataPointEntity> dataPoints;
}
