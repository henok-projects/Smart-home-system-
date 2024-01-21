package com.galsie.gcs.resources.data.entity.assetgroup;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetGroupFileEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="uniqueId")
    Long assetGroupId;

    @ManyToOne
    AssetGroupEntity assetGroup;

    @Column(name="path")
    String path;

    @Column(name="version")
    String version;

    @Column(name ="created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name ="updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

}
