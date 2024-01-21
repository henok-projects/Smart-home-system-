package com.galsie.gcs.homescommondata.data.entity.protocol.galsie;

import com.galsie.gcs.homescommondata.data.entity.protocol.common.TypedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DiverseGroupTypeEntity extends TypedEntity {
    @Builder
    public DiverseGroupTypeEntity(Long uniqueId, Long typeId, String definition, String name, boolean isSystemGroup, List<DeviceTypeEntity> possibleDeviceTypes) {
        super(uniqueId, typeId, definition, name);
        this.isSystemGroup = isSystemGroup;
        this.possibleDeviceTypes = possibleDeviceTypes;
    }

    @Column(name="is_system_group")
    boolean isSystemGroup;

    @ManyToMany // will create a new table to join them
    List<DeviceTypeEntity> possibleDeviceTypes;

    @Override
    public boolean checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(TypedEntity otherEntity) {
        // possibleDeviceTypes aren't considered external joins even though a new table is generated for it
        // this is because it is only referenced in this DiverseGroupTypeEntity, and would be created and removed through this entity.
        return super.checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(otherEntity);
    }
}
