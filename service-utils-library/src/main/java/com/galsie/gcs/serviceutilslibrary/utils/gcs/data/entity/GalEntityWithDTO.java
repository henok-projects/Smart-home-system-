package com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity;

public interface GalEntityWithDTO<idType, T> extends GalEntity<idType>{

    public T toDTO();

}
