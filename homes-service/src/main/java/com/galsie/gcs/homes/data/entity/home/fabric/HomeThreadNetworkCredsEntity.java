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
public class HomeThreadNetworkCredsEntity implements GalEntity<Long> {
    @Id
    @Column(name="unique_id")
    Long uniqueId;

    @ManyToOne // a home currently sypports one thread network. It may have multiple thread networks in the future, so account for it
    HomeEntity home;

    @Column(name="op_code")
    String opCode;

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
        modifiedAt = LocalDateTime.now();
    }
}
