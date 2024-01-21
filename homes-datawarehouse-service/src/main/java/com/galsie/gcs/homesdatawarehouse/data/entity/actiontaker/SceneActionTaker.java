package com.galsie.gcs.homesdatawarehouse.data.entity.actiontaker;

import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.operators.AbstractHomeSceneEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/*
    A SceneActionTaker can only take actions if it was initiated by another ActionTaker (user, assistant, another scene, automation, ....)

 */
public class SceneActionTaker {

    @ManyToOne
    @JoinColumn(name="associated_scene_unique_id")
    AbstractHomeSceneEntity scene; // which scene is this action taker. ManyToOne since a scene will be run many times

    @ManyToOne
    @JoinColumn(name="scene_action_taker_id")
    ActionTakerEntity initiatedBy; // an action taker could initiate multiple scene actions


}
