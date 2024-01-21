package com.galsie.gcs.homescommondata.data.entity.protocol.galsie;

import com.galsie.gcs.homescommondata.data.entity.protocol.common.TypedEntity;
import com.galsie.gcs.homescommondata.data.entity.protocol.matter.MTRDeviceTypeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DeviceTypeEntity extends TypedEntity {
    @Builder
    public DeviceTypeEntity(Long uniqueId, Long typeId, String definition, String name, CategoryTypeEntity categoryType,List<MTRDeviceTypeEntity> possibleMtrDevices) {
        super(uniqueId, typeId, definition, name);
        this.categoryType = categoryType;
        this.possibleMtrDevices = possibleMtrDevices;
    }

    @ManyToOne
    CategoryTypeEntity categoryType;

    @ManyToMany
    List<MTRDeviceTypeEntity> possibleMtrDevices;

    @Override
    public boolean checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(TypedEntity otherEntity) {
        // possibleFeatureTypes & possibleMtrDevices aren't considered external joins even though a new table is generated for them
        // this is because they are only referenced in this DeviceTypeEntity, and would be created and removed through this entity.
        var castEnt = (DeviceTypeEntity) otherEntity;
        return super.checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(otherEntity);
    }

}
