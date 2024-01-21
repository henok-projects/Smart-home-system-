package com.galsie.gcs.resources.data.entity.sync;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a synchronization entity responsible for managing the synchronization of files from GalAssets.
 * It is associated with an entity that stores the data of each file requiring synchronization @{link GalAssetsSynchronizedFileEntity}.
 * The primary reason for creating this synchronization entity is to guarantee data consistency even when assets in GalAssets undergo changes. By maintaining an association with the specific data entity for each file, we ensure that the synchronization request will consistently return the same data as it was at the time the request was made.
 * This ensures the prevention of any potential issues that may arise due to concurrent changes or updates to assets during synchronization.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GalAssetsSynchronizationEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="unique_id")
    Long id;

    @OneToMany
    @JoinTable(name="gal_assets_synchronization_entity_and_file_entity")
    List<GalAssetsSynchronizedFileEntity> galAssetsSynchronizedFileEntities;

}
