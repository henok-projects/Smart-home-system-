package com.galsie.gcs.homesdatawarehouse.data.entity.devicedata;

import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.AbstractHomeDeviceEntity;
import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.HomeDeviceMTRClusterDataEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

/*
Background:
- A home device has a list of features, each holding attributes. Each attribute has a datatype, and a history of data-points

HomeDeviceDataEntity:
- Each device

 */
@Entity
@Getter
public class HomeDeviceDataEntity extends AbstractHomeDeviceEntity {

    @Id
    @Column(name="device_data_id")
    Long uniqueId;

    @OneToOne
    @MapsId
    @JoinColumn(name="device_data_id")
    AbstractHomeDeviceEntity homeDeviceEntity; // the device which data is associated withs

    @OneToMany(mappedBy = "deviceData")
    List<HomeDeviceMTRClusterDataEntity> homeDeviceClusterDataEntities;

}
