package com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.analogue;


import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.AttributeDataPointValueEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
public class BoundedAnalogueAttributeDataPointValueEntity extends AttributeDataPointValueEntity implements AnalogueAttributeDataPointValueEntity {

    @Column(name="value")
    private Double value;
}
