package com.galsie.gcs.homescommondata.bootstrap.helpers;

import com.galsie.gcs.homescommondata.data.entity.protocol.common.TypedEntity;
import com.galsie.gcs.homescommondata.repository.common.TypedEntityRepository;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.UpdateEntityRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * EntityBootstrapService:
 *  - Bootstrap entities from a non-database data entity to the database
 *
 * TypedEntityBootstrapService:
 *  - Bootstraps TypedEntities from a non-database data entity into database
 *
 * Architecture Note:
 *  - TypedEntities have a typeId that should uniquely identify the type itself.
 *  - It is not the uniqueId of TypedEntity though since we archive old TypedEntities
 *
 *
 *  Finding the associated database entity:
 *    - get all non-archived matching the id using repository.findAllbyTypeIdAndArchived
 *    - should match using BootstrapEntityMatcher.matchesSpecificExcludingExternalJoinsAndUID
 *      - Ones that don't match using BootstrapEntityMatcher are archived since they have the same type id but different data
 * @return
 */
public class TypedEntityBootstrapService<dataObjectType, entType extends TypedEntity> extends EntityBootstrapService<dataObjectType, TypedEntityRepository<entType>, entType, Long>{
    private static final Logger logger = LogManager.getLogger();

    public TypedEntityBootstrapService(TypedEntityRepository<entType> repository, UpdateEntityRunnable<entType> updateEntityRunnable) {
        super(repository, (dataEnt) -> repository.findAllByTypeIdAndArchived(dataEnt.getTypeId(), dataEnt.isArchived()), (dbEnt, dataEnt) -> dbEnt.checkShouldArchiveByMatchingAgainstDataEntityIgnoringExternalJoinsAndUID(dataEnt), (dbEnt, dataEnt) -> {
            var updated = false;
            if (dbEnt.getName().equals(dataEnt.getName())){ // for typed entities, if the name changed, update it. This is accounted for in checkForArchive where it wouldn't return true if the name changes
                dbEnt.setName(dataEnt.getName());
                updated = true;
            }
            return updateEntityRunnable.updateEntity(dbEnt, dataEnt) || updated; // order of conditions must remain as such or  updateEntityRunnable.updateEntity wouldn't be called
        }, TypedEntity::getDefinition);
    }
}
