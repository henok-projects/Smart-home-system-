package com.galsie.gcs.resources.data.entity.gitsync;

import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceStatus;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GalResourceGitSynchronizationEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id", nullable = false)
    private Long id;

    @Column(name="resource_manager_status")
    private GCSResourceMicroserviceStatus resourceManagerStatus;

    @Column
    private GCSMicroservice gcsMicroservice;

    @Column
    private String instanceId;

    @Column(name="current_commit_sha")
    private String currentCommitSHA;

}
