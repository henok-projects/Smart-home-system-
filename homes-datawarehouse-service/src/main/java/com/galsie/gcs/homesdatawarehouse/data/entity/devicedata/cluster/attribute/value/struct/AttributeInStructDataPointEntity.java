package com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.struct;

import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.AttributeDataPointValueEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.Getter;

import javax.persistence.*;

/*
AttributeInStructDataPointEntity is a data point for a structure based attribute
 */
@Entity
@Getter
public class AttributeInStructDataPointEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;

    @ManyToOne
    StructBasedAttributeDataPointValueEntity forStructDataPointValue;

    @OneToOne(mappedBy = "forAttrInStructDataPoint") // joined in AttributeDataPointValueEntity
    AttributeDataPointValueEntity attributeDataPointValue;

}
