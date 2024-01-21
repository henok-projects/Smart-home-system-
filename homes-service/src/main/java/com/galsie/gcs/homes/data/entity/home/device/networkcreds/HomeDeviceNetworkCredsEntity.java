package com.galsie.gcs.homes.data.entity.home.device.networkcreds;

import com.galsie.gcs.homes.data.entity.home.device.HomeDeviceEntity;
import com.galsie.gcs.homes.data.entity.home.fabric.HomeThreadNetworkCredsEntity;
import com.galsie.gcs.homes.data.entity.home.fabric.HomeWifiNetworkCredsEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.Getter;

import javax.persistence.*;

/*
Associates a home device with a Wifi and/or thread network credentials
- Note: If associated with both, its supposed to be a thread border router.
 */
@Entity
@Getter
public class HomeDeviceNetworkCredsEntity implements GalEntity<Long> {

    @Id
    @Column(name="home_device_id")
    Long homeDeviceId;

    @OneToOne
    @MapsId
    HomeDeviceEntity homeDevice;

    @OneToOne
    @JoinColumn(name="thread_network_creds")
    HomeThreadNetworkCredsEntity threadNetworkCreds;


    @OneToOne
    @JoinColumn(name="wifi_network_creds")
    HomeWifiNetworkCredsEntity wifiNetworkCreds;

    @PrePersist
    @PreUpdate
    protected void onCreateOrModify() throws Exception{
      /*  if (threadNetworkCreds == null && wifiNetworkCreds == null){
            throw new Exception("HomeDeviceNetworkCredsEntity doesn't have thread or wifi credential entities");
        }*/
    }
    /*
    public DeviceNetworkConnectionType getNetworkCredsType(){
        if (threadNetworkCreds != null && wifiNetworkCreds != null){
            return DeviceNetworkConnectionType.WIFI_AND_THREAD;
        }
        return threadNetworkCreds == null ? DeviceNetworkConnectionType.WIFI : DeviceNetworkConnectionType.THREAD;
    }*/
}
