package com.galsie.gcs.smartdevicesservice.data.entity.galdevice;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.smartdevicesservice.data.discrete.GalDeviceProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GalDeviceEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uniqueId;

    @Column(name="product_type")
    GalDeviceProductType galDeviceProductType;


    @Column(name="serial_num", unique = true)
    String serialNumber; // every device has a serial number. the format of which is to be determined.
}
