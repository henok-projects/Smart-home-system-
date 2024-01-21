package com.galsie.gcs.homescommondata.data.entity.protocol.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@MappedSuperclass // MappedSuperclass means that subclass entities inherit the parameters without actual table inheritence
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public abstract class TypedEntity extends BootstrappedEntity<Long> implements BootstrappedEntityMatcher<TypedEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;

    @Column(name="type_id")
    Long typeId;

    @Column(name="definition")
    String definition;

    @Column(name="name")
    String name; // note: name can be updated by bootstrap service

    @Override
    public Long getEntityId(){
        return uniqueId;
    }

    /**
     * WithoutExternalJoins: Matches without any Joins who's column reference is stored in another entity
     * WithoutUniqueId: Mathces without the unique id
     *
     * Subclasses must override this method and call super with additional comparisons
     * @param otherEntity: the other Entity to match to
     * @return
     */
    @Override
    public boolean checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(TypedEntity otherEntity){
        // ignore name since we wouldn't archive in that case, rather update the name. This is accounted for in TypedEntityBootstrapService
        return otherEntity.typeId.equals(this.typeId) && otherEntity.definition.equals(this.definition);
    }
}
