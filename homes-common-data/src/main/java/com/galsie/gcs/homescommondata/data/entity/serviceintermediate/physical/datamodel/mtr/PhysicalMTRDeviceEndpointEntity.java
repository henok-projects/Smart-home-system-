package com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.mtr;

import com.galsie.gcs.homescommondata.data.entity.protocol.matter.MTRDeviceTypeEntity;
import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.PhysicalDeviceDataModelRevisionEntity;
import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.gal.PhysicalDeviceGalDeviceTypeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.lib.utils.lang.Nullable;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
public class PhysicalMTRDeviceEndpointEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;

    @ManyToOne
    PhysicalDeviceDataModelRevisionEntity revisionEntity;
    /**
     * An endpoint may have multiple device types (eg: Light + Occupancy sensor)
     */
    @ManyToMany
    @JoinColumn(name="mtr_device_type") // create a join column for the same reason as above
    List<MTRDeviceTypeEntity> mtrDeviceType;

    @OneToMany
    List<PhysicalMTRDeviceClusterEntity> mtrDeviceTypeClusters;

    /**
     * The endpoint may not be mappable to a Galsie device type
     */
    @Nullable
    @ManyToOne(optional = true)
    PhysicalDeviceGalDeviceTypeEntity galDeviceTypeEntity;

}
