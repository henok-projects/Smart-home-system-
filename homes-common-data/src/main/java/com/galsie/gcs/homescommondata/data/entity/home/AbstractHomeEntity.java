package com.galsie.gcs.homescommondata.data.entity.home;


import com.galsie.gcs.homescommondata.data.entity.home.user.AbstractHomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 There MUST ONLY exist one child of this entity. The reason it exists here is to act as an intermediate.

 The logic is:

 We have an Entity here with a SINGLE_TABLE inheritance strategy
 This entity can now be accessed by all microservices which depend on homes-common-data
 One of these microservice types (the home-service) defines a HomeEntity which extends this AbstractHomeEntity populating it with extra data.
 The homes-service would be responsible for creating instances of this entity
 Other microservices can only access the data defined in AbstractHomeEntity - thus hiding un-needed data from them
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractHomeEntity implements GalEntity<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_id")
    Long homeId;

    @OneToMany(mappedBy = "home")
    List<AbstractHomeUserEntity> abstractHomeUserEntities;


}


