package com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity;

/*
A microservice might need to reference an entity from another microservice.
- The use case is joining that entity with entities in this microservice.
To do so, a persistence entity implements GalRemoteEntity.


Note: GalRemoteEntity does no operations at all, it exists for the sake of easy scalability and implementation of common functionalities
 */
public interface GalRemoteEntity<idType> extends GalEntity<idType>{

    idType getReferencedEntityId();
}
