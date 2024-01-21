package com.galsie.gcs.homesdatawarehouse.data.entity.actiontaker;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/*
    An AssistantActionTaker can only take actions if it was initiated by another ActionTaker (scene, user, assistant, ...)
 */

@Entity
public class AssistantActionTakerEntity extends ActionTakerEntity {


    @ManyToOne
    @JoinColumn(name="assistant_action_taker_id")
    ActionTakerEntity initiatedBy; // an action taker could initiate multiple assistant actions


}
