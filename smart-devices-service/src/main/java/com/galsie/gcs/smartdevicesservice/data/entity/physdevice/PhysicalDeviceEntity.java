package com.galsie.gcs.smartdevicesservice.data.entity.physdevice;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
A PhysicalDeviceEntity represents a physical device that exists.
- It can be either a GalDevice (GalPhysicalDeviceEntity) or another Matter supported device (MTRPhysicalDeviceEntity)

 Note: While Galsie works with matter, the associations are kept separate since GalDevices are superior and can connect to 'continuous-service'
 Note: PhysicalDeviceEntity is created once a device is first scanned for commissioning. (the entity is saved evem if commissioning fails)

 Note: homes-data-common holds a PhysicalDeviceRemoteEntity that references the id of this PhysicalDeviceEntity
 */
@Inheritance(strategy = InheritanceType.JOINED) // joined will have tables for each of the child entities, joined with a parent PhysicalDeviceEntity
public class PhysicalDeviceEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="phys_device_id")
    Long id;

    @Column
    LocalDateTime creationTimestamp; /* Store when the entity was created, could be useful */

    @PrePersist
    void onCreate(){
        this.creationTimestamp = LocalDateTime.now();
    }
}
