package com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical;


import com.galsie.gcs.homescommondata.data.discrete.HomeDeviceStatus;
import com.galsie.gcs.homescommondata.data.discrete.PhysicalDeviceCommissionStatus;
import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.AbstractHomeDeviceEntity;
import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.PhysicalDeviceDataModelRevisionEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalRemoteEntity;
import lombok.*;
import javax.persistence.*;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PhysicalDeviceRemoteEntity implements GalRemoteEntity<Long> {
    /*
        PhysicalDeviceRemoteEntity (and all other remote entities) is associated with the remote entity through phys_device_id
        - The remote entity it is associated with is PhysicalDeviceEntity in smartdevice-servic

        NOTE: Must ensure they are kept synchronised
     */
    @Id
    @Column(name = "phys_device_id", nullable = false)
    Long physicalDeviceId;

    @Column(name = "status")
    PhysicalDeviceCommissionStatus status; // updated automatically when entity is updated. Check this.onUpdate

    @OneToMany(mappedBy = "baseDevice")
    List<AbstractHomeDeviceEntity> homeDevices; // can only be commissioned for one at once, the remaining are for history

    /**
     * The data model of a device may undergo many revisions.
     */
    @OneToMany(mappedBy = "physicalDevice")
    List<PhysicalDeviceDataModelRevisionEntity> deviceModel;

    @Column
    Long lastCommissionTimestamp; // the timestamp it was last commissioned

    @Column
    Long lastDecommissionTimestamp; // the timestamp it was last decomissioned. It may be before lastCommissionTimestamp if someone decommissioned, then another commissioned.

    @PreUpdate
    protected void onUpdate() {
        long commissionedCount = homeDevices.stream().filter((homeDeviceEntity) -> homeDeviceEntity.getStatus() != HomeDeviceStatus.DECOMMISSIONED).count();
        status = commissionedCount == 0 ? PhysicalDeviceCommissionStatus.DECOMISSIONED : (commissionedCount == 1 ? PhysicalDeviceCommissionStatus.COMMISSIONED: PhysicalDeviceCommissionStatus.ERROR_COMMISSIONED_IN_MULTIPLE);
    }

    @Override
    public Long getReferencedEntityId() {
        return this.physicalDeviceId;
    }
}