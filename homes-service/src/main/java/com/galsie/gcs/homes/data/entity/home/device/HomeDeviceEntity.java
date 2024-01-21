package com.galsie.gcs.homes.data.entity.home.device;

import com.galsie.gcs.homes.data.entity.home.area.HomeAreaEntity;
import com.galsie.gcs.homes.data.entity.home.device.networkcreds.HomeDeviceNetworkCredsEntity;
import com.galsie.gcs.homescommondata.data.discrete.HomeDeviceStatus;
import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.AbstractHomeDeviceEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeDeviceEntity implements GalEntity<Long> {
    @Id
    @Column(name="intermediate_unique_id", nullable = false)
    Long intermediate_unique_id;

    @OneToOne
    @MapsId
    AbstractHomeDeviceEntity homeDeviceIntermediate;

    @OneToOne(mappedBy = "homeDevice")
    HomeDeviceNetworkCredsEntity networkCredsEntity;

    @ManyToOne
    private HomeAreaEntity area;

    @Column(name="commissioned_at")
    private LocalDateTime commissionedAt;

    @Column(name="decommissioned_at", nullable = true)
    LocalDateTime decommissionedAt; // if this value is set, and it is > commissionedAt time, the device is considered decommissioned

    @Column(name="created_at")
    LocalDateTime createdAt;

    @Column(name="modified_at")
    LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onPreUpdate(){

        if (this.isDecommissioned() && homeDeviceIntermediate.getStatus() != HomeDeviceStatus.DECOMMISSIONED){
            // TODO: CHECK IF ACTUALLY UpdATES
            homeDeviceIntermediate.setStatus(HomeDeviceStatus.DECOMMISSIONED);
        }
        modifiedAt = LocalDateTime.now();
    }

    /*
    Device is considered decommissioned when decommissionedAt is set and is > commissionedAt
     */
    boolean isDecommissioned(){
        return this.decommissionedAt != null; // && Duration.between(commissionedAt, decommissionedAt).isPositive(); TODO: FIX
    }
}
