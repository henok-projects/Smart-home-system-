package com.galsie.gcs.homescommondata.data.entity.home.user;

import com.galsie.gcs.homescommondata.data.entity.home.AbstractHomeEntity;
import com.galsie.gcs.homescommondata.data.entity.user.AppUserRemoteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.*;

/**
 There MUST ONLY exist one child of this entity. The reason it exists here is to act as an intermediate.

 The logic is:

 We have an Entity here with a SINGLE_TABLE inheritance strategy
 This entity can now be accessed by all microservices which depend on homes-common-data
 One of these microservice types (the home-service) defines a HomeUserEntity which extends this AbstractHomeUserEntity populating it with extra data.
 The homes-service would be responsible for creating instances of this entity
 Other microservices can only access the data defined in AbstractHomeUserEntity - thus hiding un-needed data from them
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class AbstractHomeUserEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long uniqueId;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    AppUserRemoteEntity appUser;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "home_id")
    AbstractHomeEntity home;
}
