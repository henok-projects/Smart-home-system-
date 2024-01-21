package com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity;

public interface UpdateEntityRunnable<T extends GalEntity<?>> {

    /**
     *
     * @param databaseEntity The entity in the database
     * @param newEntity the new entity object that would have been added to the database had the databaseEntity not existed
     * @return true if the databaseEntity was updated
     *  When Implemented, the body of the method should update attributes of databaseEntity to match newEntity
     */
    public boolean updateEntity(T databaseEntity, T newEntity);

}
