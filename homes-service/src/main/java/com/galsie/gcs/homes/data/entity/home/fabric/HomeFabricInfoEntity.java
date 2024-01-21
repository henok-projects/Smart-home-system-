package com.galsie.gcs.homes.data.entity.home.fabric;

import com.galsie.gcs.homes.data.entity.home.HomeEntity;
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
public class HomeFabricInfoEntity implements GalEntity<Long> {

    public static int IPK_LENGTH = 128; // 128 bits -> 16 bytes
    public static int PRIVATE_KEY_LENGTH = 248; // 128 bits -> 16 bytes
    @Id
    @Column(name="home_id")
    Long homeId;
    @OneToOne
    @MapsId
    HomeEntity home;

    @Column(name="ipk")
    byte[] ipk;

    @Column(name="private_key")
    byte[] privateKey;

    @Column(name="ipk_modified_at")
    LocalDateTime ipkModifiedAt;

    @Column(name="pk_modified_at")
    LocalDateTime pkModifiedAt; // private key modified at
}
