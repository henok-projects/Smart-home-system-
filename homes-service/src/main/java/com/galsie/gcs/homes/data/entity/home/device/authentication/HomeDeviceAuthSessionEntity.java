package com.galsie.gcs.homes.data.entity.home.device.authentication;

import com.galsie.gcs.homes.data.entity.home.device.HomeDeviceEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


/*
A HomeDeviceAuthSessionEntity is used to authenticate GalDevices (Galsie's Manufactured devices):
 - created when a device is commissioned
 - destroyed and recreated when device connects
 - destroyed when there are MAJOR security concerns,

Note: If a token is destroyed before a new one is generated and sent to the device, the device will lose access.
 - By system design, for security reasons, we destroy the token immediately if there is some cyber security issue.
 - This means that if a device connected with a destroyed token, or any token older than the destroyed token, we should reject it
 - We do not generate a new token, the user must locally connect to the network cluster of the device and requests to send it a new token.

Note: Used by 'continuous-service'. Might be modified in the future. // TODO: look out for that

 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeDeviceAuthSessionEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="home_device_auth_session_id")
    Long id;

    @ManyToOne // a device may have many auth session entities. Only the most recently generated one is valid.
    HomeDeviceEntity homeDeviceEntity;

    @Column
    String accessToken;

    @Column
    LocalDateTime generatedAt;

    @Column
    LocalDateTime destroyedAt;

    @PrePersist
    void onCreate(){
        generatedAt = LocalDateTime.now();
    }

}
