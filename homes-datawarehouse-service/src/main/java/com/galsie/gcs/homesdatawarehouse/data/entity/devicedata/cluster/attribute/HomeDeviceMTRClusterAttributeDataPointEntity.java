package com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute;

import com.galsie.gcs.homesdatawarehouse.data.entity.actiontaker.ActionTakerEntity;
import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.attribute.value.AttributeDataPointValueEntity;
import com.galsie.gcs.homesdatawarehouse.data.entity.devicedata.cluster.HomeDeviceMTRClusterDataEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.lib.utils.lang.NotNull;
import com.galsie.lib.utils.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HomeDeviceMTRClusterAttributeDataPointEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;


    @ManyToOne
    HomeDeviceMTRClusterDataEntity mtrClusterDataEntity;

    @NotNull
    @ManyToOne(optional = false)
    ActionTakerEntity actionTaker; // who took the action that resulted in this attribute data point? The ActionTaker may have taken multiple actions at the same time, thats why its many to one

    @NotNull
    @Column(nullable = false)
    String serializedValue;

    /**
     * For some attributes, we store the typed value, as it's more convenient to query
     */
    @Nullable
    @OneToOne(optional = true)
    AttributeDataPointValueEntity attributeDataPointValueEntity;

    /**
     * - We create a new data point when the attribute value changes
     * - We set it's created_at time to the time that the attribute value changed
     */
    @Column(name="created_at")
    LocalDateTime created_at;

    /**
     * We update this time if the attribute value is still the same
     */
    @Column(name="updated_at")
    LocalDateTime updated_at;

}
