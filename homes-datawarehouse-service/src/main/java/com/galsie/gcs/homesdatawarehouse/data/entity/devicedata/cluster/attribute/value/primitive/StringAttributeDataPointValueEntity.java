package com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.primitive;

import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.AttributeDataPointValueEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
public class StringAttributeDataPointValueEntity extends AttributeDataPointValueEntity {

    @Column(name="value")
    String value;
}
