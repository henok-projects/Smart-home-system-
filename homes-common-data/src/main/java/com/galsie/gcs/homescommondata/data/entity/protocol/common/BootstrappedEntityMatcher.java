package com.galsie.gcs.homescommondata.data.entity.protocol.common;

/**
    Used by some entities that needs to be bootstrapped from some data. Eg: from xml data.

 BootstrappedEntityMatcher#checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID compares a db entity to a dataEntity
    - In the event that they match, the database entity might be updated with some attributes
    - In the event they don't, the database entity is archived, and a new entity is created.

 BootstrappedEntityMatcher Matches the entity attributes that if they change, the entity has to be archived
    - Meaning if BootstrapEntityMatcher matches, the entity shouldn't archive, rather update.
 */
public interface BootstrappedEntityMatcher<T extends BootstrappedEntityMatcher<T>> {


    /**
     * Matches the entity attributes that make the entity unique in the sense that if those attributes changed, the entity has to be archived.
     * - There shouldn't be 2 unarchived entities that match the same otherEntity.
     *
     * @param otherEntity The other entity to match attributes to
     * @return True if there is a mismatch and the entity needs to be archived. False if they match and the entity simply needs syncrhonization
     */
    boolean checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(T otherEntity);


    /*
        If the collections have the same elements by checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID

    static <childT extends parentT, parentT extends BootstrappedEntityMatcher<parentT>> boolean checkShouldArchiveByMatchingDbCollectionAgainstDataCollection(List<childT> first, List<childT> dataCollection) {
        return first.size() == dataCollection.size() && first.stream().allMatch((a) -> dataCollection.stream().anyMatch((b) -> b.checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(a)));
    }     */
}
