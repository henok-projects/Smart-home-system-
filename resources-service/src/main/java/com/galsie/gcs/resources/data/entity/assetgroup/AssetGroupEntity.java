package com.galsie.gcs.resources.data.entity.assetgroup;


import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetGroupEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long id;

    @Column(name="asset_group_type", unique = true, nullable = false)
    AssetGroupType assetGroupType;

    /*
     The last time the asset group files where updated.. Needed so that other services can synchronize the asset group
     */
    @Column(name="last_update")
    LocalDateTime lastUpdate;

    /*
         Is the last update on the asset group required. IE: Must other services synchronize, or should they do it at their convenience.
     */
    @Column(name="last_update_required")
    boolean lastUpdateRequired;

    @OneToMany(mappedBy = "assetGroup")
    List<AssetGroupFileEntity> assetGroupFiles;

}
