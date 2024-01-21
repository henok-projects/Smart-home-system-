package com.galsie.gcs.homescommondata.data.entity.protocol.galsie;

import com.galsie.gcs.homescommondata.data.entity.protocol.common.TypedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CategoryTypeEntity extends TypedEntity {

    @Builder
    public CategoryTypeEntity(Long uniqueId, Long typeId, String definition, String name, boolean isAbstract, List<DeviceTypeEntity> deviceTypes) {
        super(uniqueId, typeId, definition, name);
        this.deviceTypes = deviceTypes;
        this.isAbstract = isAbstract;
    }


    @Column(name="is_abstract")
    boolean isAbstract;

    @OneToMany(mappedBy = "categoryType")
    List<DeviceTypeEntity> deviceTypes;

    @ManyToOne
    @JoinColumn(name="extends_category_id")
    CategoryTypeEntity extendsCategory;

    @Override
    public boolean checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(TypedEntity otherEntity) {
        return super.checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(otherEntity); // needn't add anything since deviceTypes is an external join
    }
}
