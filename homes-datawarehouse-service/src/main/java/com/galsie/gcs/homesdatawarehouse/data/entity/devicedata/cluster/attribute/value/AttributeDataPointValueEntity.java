package com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value;

import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.struct.AttributeInStructDataPointEntity;
import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.HomeDeviceMTRClusterAttributeDataPointEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Joined so that AttributeDataPointValueEntity has a table of primary unique ids to joined to its subclasses with that primary key
public abstract class AttributeDataPointValueEntity implements GalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;

    /**
        An attribute may associate with HomeDeviceAttributeDataPointEntity, which is an attribute belonging to a feature directly
        NOTE: If this is non-nil, forAttrInStructDataPoint must be nil
     */
    @OneToOne
    @JoinColumn(name="attribute_data_point_value_id", nullable = true)
    HomeDeviceMTRClusterAttributeDataPointEntity forClusterAttributeDataPoint;

    /**
     * An attribute may associate with AttributeInStructDataPointEntity which is an attribute belonging to a structure which would belong to a feature directly or another structure until it belongs to a feature
     *  NOTE: If this is non-nil, forClusterAttributeDataPoint must be nil
     */
    @OneToOne
    @JoinColumn(name="attr_in_struct_data_point_id", nullable = true)
    AttributeInStructDataPointEntity forAttrInStructDataPoint;

    public boolean isForAttrInStruct(){
        return forAttrInStructDataPoint != null;
    }

    public boolean isForClusterAttributeDataPoint(){
        return forClusterAttributeDataPoint != null;
    }

    @PrePersist
    public void onCreate() throws Exception{
        if (isForClusterAttributeDataPoint() && isForAttrInStruct()){
            throw new Exception("Attribute Data Point Value must be either for a HomeDeviceAttributeDataPointEntity or AttributeInStructDataPointEntity not BOTH");
        }
        if (!isForClusterAttributeDataPoint() && !isForAttrInStruct()){
            throw new Exception("Attribute Data Point Value must be either for a HomeDeviceAttributeDataPointEntity or AttributeInStructDataPointEntity not NEITHER");
        }
    }
}
