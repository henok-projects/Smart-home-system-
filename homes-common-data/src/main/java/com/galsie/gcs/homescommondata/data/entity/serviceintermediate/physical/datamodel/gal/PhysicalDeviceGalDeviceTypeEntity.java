package com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.gal;

import com.galsie.gcs.homescommondata.data.entity.protocol.galsie.CategoryTypeEntity;
import com.galsie.gcs.homescommondata.data.entity.protocol.galsie.DeviceTypeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.lib.utils.lang.Nullable;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class PhysicalDeviceGalDeviceTypeEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;


    @Nullable // nullable since there may not be any matching device type
    @ManyToOne
    @JoinColumn(name="device_type", nullable = true) // create a join column since deviceType dosn't have OneToMany pointing here
    DeviceTypeEntity deviceType;


    public CategoryTypeEntity getCategory(){
        return deviceType.getCategoryType();
    }

    /**
     * The feature types of this device type, mapped from certain cluster sets
     * -- they are associated with the PhysicalMTRDeviceClusterEntity

    @OneToMany(mappedBy = "deviceType")
    List<PhysicalDeviceGalFeatureTypeEntity> featureTypes;
     */

}
