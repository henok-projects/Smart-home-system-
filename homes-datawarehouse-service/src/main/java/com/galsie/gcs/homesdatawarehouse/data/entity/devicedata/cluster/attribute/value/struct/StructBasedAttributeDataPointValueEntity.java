package com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.struct;

import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.AttributeDataPointValueEntity;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
public class StructBasedAttributeDataPointValueEntity extends AttributeDataPointValueEntity {

    @OneToMany(mappedBy = "forStructDataPointValue")
    List<AttributeInStructDataPointEntity> structAttributesDataPoints;

}
