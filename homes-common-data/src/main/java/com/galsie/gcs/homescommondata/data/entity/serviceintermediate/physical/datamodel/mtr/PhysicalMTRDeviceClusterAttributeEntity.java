package com.galsie.gcs.homescommondata.data.entity.serviceintermediate.physical.datamodel.mtr;

import javax.persistence.*;

/**
 *
 */
@Entity
public class PhysicalMTRDeviceClusterAttributeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;

    /**
     * The cluster which this attribute is part of
     */
    @ManyToOne
    PhysicalMTRDeviceClusterEntity containingCluster;

    /**
     * Each attribute in a cluster has a code
     */
    @Column(name="attribute_code")
    int attributeCode;

    /**
     * The attribute may be controllable by the server (the device itself)
     * OR by a client (ie a knob that can turn on the device)
     */
    MTRAttributeControlSide side; // SERVER OR CLIENT

    /**
     * Can ADD more info here, like if its an enum, what are the various enum values?
     *
     * THIS for now will be a serialized string, managed by the front-end.
     */
    String usefulContextSerialized;
}
