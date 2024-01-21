package com.galsie.gcs.homescommondata.data.entity.protocol.matter;

import com.galsie.gcs.homescommondata.data.entity.protocol.common.TypedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MTRDeviceTypeEntity extends TypedEntity {
    @Builder
    public MTRDeviceTypeEntity(Long uniqueId, Long typeId, String definition, String name) {
        super(uniqueId, typeId, definition, name);
    }

    @Override
    public boolean checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(TypedEntity otherEntity) {
        return super.checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(otherEntity); // needn't add anything since deviceTypes is an external join
    }
}
