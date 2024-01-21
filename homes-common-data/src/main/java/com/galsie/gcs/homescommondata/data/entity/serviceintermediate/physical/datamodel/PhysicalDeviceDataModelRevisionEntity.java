package com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel;

import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.PhysicalDeviceRemoteEntity;
import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.gal.PhysicalDeviceGalDeviceTypeEntity;
import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.mtr.PhysicalMTRDeviceEndpointEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


/**
 * For every physical device, we store:
 * - The matter Endpoint & Cluster structure (without the cluster attributes)
 * - The Galsie Data model, linked to the matter data model through the matter clusters
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhysicalDeviceDataModelRevisionEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uniqueId;

    /**
     * The physical device which this data model revision is for
     */
    @ManyToOne
    PhysicalDeviceRemoteEntity physicalDevice;

    /**
     * The revision id of this data model
     */
    @Column(name="revision_id")
    int revisionId;

    @OneToMany(mappedBy = "revisionEntity")
    List<PhysicalMTRDeviceEndpointEntity> mtrDeviceEndpointEntities;




}
