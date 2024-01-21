package com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.mtr;

import com.galsie.gcs.homescommondata.data.entity.protocol.matter.MTRClusterTypeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PhysicalMTRDeviceClusterEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;

    @ManyToOne
    PhysicalMTRDeviceEndpointEntity containingEndpoint; // the endpoint which contains this cluster type

    @ManyToOne
    @JoinColumn(name="mtrCluster") // join column since there is no OneToMany in MTRClusterTypeEntity. This creates a join table that we dont worry about
    MTRClusterTypeEntity clusterType;

}
