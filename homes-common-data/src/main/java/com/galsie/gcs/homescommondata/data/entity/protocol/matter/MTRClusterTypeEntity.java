package com.galsie.gcs.homescommondata.data.entity.protocol.matter;


import com.galsie.gcs.homescommondata.data.entity.protocol.common.TypedEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class MTRClusterTypeEntity extends TypedEntity {
    @Builder
    public MTRClusterTypeEntity(Long uniqueId, Long typeId, String definition, String name) {
        super(uniqueId, typeId, definition, name);
    }

    @Override
    public boolean checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(TypedEntity otherEntity) {
        return super.checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(otherEntity); // needn't add anything since deviceTypes is an external join
    }
}
