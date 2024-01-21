package com.galsie.gcs.homesdatawarehouse.data.entity.actiontaker;

import com.galsie.gcs.homescommondata.data.entity.home.user.AbstractHomeUserEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class HomeUserActionTakerEntity extends ActionTakerEntity {

    @ManyToOne
    @JoinColumn(name="home_user_entity_unique_id") // join since the HomeUserEntityIntermediate doesn't reference HomeUserActionTaker
    AbstractHomeUserEntity abstractHomeUserEntity; // a HomeUserActionTaker taker is associated with a home user entity
}
