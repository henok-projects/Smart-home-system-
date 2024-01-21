package com.galsie.gcs.smartdevicesservice.data.entity.physdevice;

import com.galsie.gcs.smartdevicesservice.data.entity.galdevice.GalDeviceEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class GalPhysicalDeviceEntity extends PhysicalDeviceEntity {

    @OneToOne
    GalDeviceEntity galDevice;


}
