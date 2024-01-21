package com.galsie.gcs.resources.data.entity.providableassetdtos;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;

//TODO: create a proper comment when branches are merged
/**
 * This entity is used to store the information about the subscribed {@link ProvidableAssetDTOType} for a specific {@link GCSMicroservice} instance.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MicroserviceSubscribedProvidableAssetDTOEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    private Long id;

    @Column
    private ProvidableAssetDTOType subscribedProvidableAssetDTOType;

    @Column
    private GCSMicroservice microservice;

    @Column
    private String uniqueInstanceId;

    @Column
    private String version;



}
