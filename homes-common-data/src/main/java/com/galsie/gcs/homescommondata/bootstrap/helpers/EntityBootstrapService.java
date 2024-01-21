package com.galsie.gcs.homescommondata.bootstrap.helpers;


import com.galsie.gcs.homescommondata.data.entity.protocol.common.BootstrappedEntity;
import com.galsie.gcs.homescommondata.data.entity.protocol.common.TypedEntity;
import com.galsie.gcs.homescommondata.repository.common.BootstrappedEntityRepository;
import com.galsie.gcs.homescommondata.repository.common.TypedEntityRepository;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.UpdateEntityRunnable;
import com.galsie.lib.utils.functional.ThrowableBiConsumer;
import com.galsie.lib.utils.functional.ThrowableFunction;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * EntityBootstrapService aims at bootstrapping data-objects (objects that hold the data of the entities to be bootstrapped) into the database.
 * - Consideres that the data-objects may change in the future, and that if they do, we need to syncrhonize the database
 * - If a data-object was already found in the database
 * - the old entity is either archived or updated based on the extent to which it matches.
 * <p>
 * Bootstraps by:
 * - Mapping data-objects into data-entities
 * - Finding the database-entities that matches the data-entity to be bootstrapped through EntityBootstrapService#loadDatabaseEntitiesMatching
 * - The database entities only have to match the attributes that make the dataEntity unique relative to other dataeEtities
 * - This varies from based on the Entity type (table), thats why the loader function is passed in the constructor 'loadDatabaseEntitiesMatching'
 * - If more than one database entity was found, archive all
 * - based on what was mentioned above, if more than one were found, this means a duplicate exist
 * - If only one was found, either archive or update
 * - The dataEntity may have attributes that don't uniquely identify it which don't match the database entity
 * - For some attributes which have changed, we archive the entity (this is because updating these attributes may result in losing data  which depends on that entity - such has a device data history - even though they don't uniquely identify the entity)
 * - EntityBootstrapService#shouldArchiveDatabaseEntity checks
 * - for other attributes, we simply update the database-entity through EntityBootstrapService#UpdateEntityRunnable
 *
 * @param <dataObjectType> The data-object that holds the entity bootstrap data.
 * @param <repoType>       The repository that handles the entities
 * @param <entType>        The type of entity
 * @param <idType>         The id type of the entity
 */
@AllArgsConstructor
public class EntityBootstrapService<dataObjectType, repoType extends BootstrappedEntityRepository<entType, idType>, entType extends BootstrappedEntity<idType>, idType> {
    static Logger logger = LogManager.getLogger();

    repoType repository;
    Function<entType, List<entType>> loadDatabaseEntitiesMatching;
    /*
        Function arguments: DB entity, second: non-db Entity
     */
    BiFunction<entType, entType, Boolean> shouldArchiveDatabaseEntity;

    UpdateEntityRunnable<entType> updateEntityRunnable;

    /*
    Some string that uniquely identifies a non-database and database entity for logging
     */
    Function<entType, String> getEntityLoggerIdentifier;


    /**
     * 1. Starts from a list of data-objects
     * 2. Maps them into data-entities through baseDataToEntityMapper
     * 3. Bootstraps the data-entities through calling this.bootstrap on each data-entity
     * 4. After each entity is bootstrapped, afterEntityBootstrapped is called on every data-object and entity
     * 5. Once done, if archiveAllOtherEntities is set to true, archives all old database-entities that weren't bootstrapped
     *
     * @param dataObjects             A list containing data-objects that would be mapped to data-entities to be bootstrapped
     * @param baseDataToEntityMapper  A function that maps a data-object into a data-entity
     * @param afterEntityBootstrapped A function that expects 2 parameters: data-object database-entity. called after the data-object is bootstrapped. Usually through this method, other entities that depend on this entity are bootstrapped
     * @param archiveAllOtherEntities If true, all other entities in the database that don't match the unique-ids of the bootstrapped entities are achived
     * @return The list of bootstrapped entities
     */
    public List<entType> bootstrapManyDataObjects(List<dataObjectType> dataObjects, ThrowableFunction<dataObjectType, entType> baseDataToEntityMapper, ThrowableBiConsumer<dataObjectType, entType> afterEntityBootstrapped, boolean archiveAllOtherEntities) throws Exception {
        var result = new ArrayList<entType>();
        for (var baseData : dataObjects) {
            var dataEnt = baseDataToEntityMapper.apply(baseData);
            var dbEnt = this.bootstrapDataEntity(dataEnt);
            afterEntityBootstrapped.consume(baseData, dbEnt);
            result.add(dbEnt);
        }
        if (archiveAllOtherEntities) {
            archiveAllEntitiesExcept(result);
        }
        return result;
    }

    public void archiveAllEntitiesExcept(List<entType> dbEntities) throws Exception {
        var otherEnts = repository.findAllByUniqueIdsNotMatching(dbEntities.stream().map(BootstrappedEntity::getEntityId).collect(Collectors.toList()));
        otherEnts.forEach((oEnt) -> oEnt.setArchived(true));
        var saveResp = GCSResponse.saveEntities(repository, otherEnts);
        if (saveResp.hasError()) {
            throw new Exception("Error while bootstrapping many entities: Failed to save archive otherEntities, reason: " + saveResp.getGcsError().getErrorMsg());
        }
    }

    /**
     * Bootstrapping a new data-entity:
     * - Search for any non-archived entities in the database that match through this.loadDatabaseEntitiesMatching
     * - If one is found
     * - If it should be archived, archive
     * - Else call updateEntityRunnable & FINISH
     * - If more than one is found, archive all (if more than one were found a duplicate exists)
     * - At this point, there were no entities found, any found entities were archived for the reasons above.
     * - In that case, create a new entity
     *
     * @param dataEntity A non-database entity that hold most recent data
     * @return The most up-to-date database-entity
     * @throws Exception if fails to save to database
     */
    public entType bootstrapDataEntity(entType dataEntity) throws Exception {
        var loggerIdentifier = dataEntity.getClass().getName() + " " + getEntityLoggerIdentifier.apply(dataEntity);
        if (dataEntity.getEntityId() != null) {
            throw new Exception("Error while bootstrapping entity " + loggerIdentifier + ": dataEntity has a uniqueId, is it already in the database? It shouldn't be.");
        }
        var foundEntities = loadDatabaseEntitiesMatching.apply(dataEntity);
        var size = foundEntities.size();
        if (size == 1) { // if one is found, compare. If it matches, finish
            var found = foundEntities.get(0);
            if (!shouldArchiveDatabaseEntity.apply(found, dataEntity)) { // if shouldn't archive
                if (updateEntityRunnable.updateEntity(found, dataEntity)) { // update
                    var resp = GCSResponse.saveEntity(repository, found);
                    if (resp.hasError()) {
                        throw new Exception("Error while bootstrapping entity " + dataEntity.getClass().getName() + " " + loggerIdentifier + ": Failed to save entities, reason: " + resp.getGcsError().getErrorMsg());
                    }
                }
                return found;
            }
            // if it should archive, warn
            logger.warn("Warning while bootstrapping entity " + loggerIdentifier + ": Entity should be archived, archiving and creating new.");
        }
        if (foundEntities.size() > 1) {
            logger.warn("Warning while bootstrapping entity " + loggerIdentifier + ": found many matching entities - for some reason, duplicates exist. Archiving all and creating new");
        }
        // 0 or more than one foundEntities exist, archive all
        foundEntities.forEach((f) -> f.setArchived(true));
        foundEntities.add(dataEntity); // add the data entity
        var saveResp = GCSResponse.saveEntities(repository, foundEntities);
        if (saveResp.hasError()) {
            throw new Exception("Error while bootstrapping entity " + loggerIdentifier + ": Failed to save entities, reason: " + saveResp.getGcsError().getErrorMsg());

        }
        return dataEntity;
    }

    /*
        Aux method
     */
    public static <T extends TypedEntity, G extends TypedEntityRepository<T>> T getEntityThrows(G repository, long typeId) throws Exception {
        var opt = repository.findAllByTypeIdAndArchived(typeId, false).stream().findFirst();
        if (opt.isEmpty()) {
            throw new Exception("Failed to get entity of typeid " + typeId + " from repository " + repository.getClass().getName());
        }
        return opt.get();
    }


}
