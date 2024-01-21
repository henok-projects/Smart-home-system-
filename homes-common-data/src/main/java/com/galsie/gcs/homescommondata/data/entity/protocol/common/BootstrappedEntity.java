package com.galsie.gcs.homescommondata.data.entity.protocol.common;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BootstrappedEntity<idType> implements GalEntity<idType> {

    @Column(name="archived")
    protected boolean archived=false; // Bootstrap archives entities that no longer match the default bootstrap data


    public abstract idType getEntityId();

}
