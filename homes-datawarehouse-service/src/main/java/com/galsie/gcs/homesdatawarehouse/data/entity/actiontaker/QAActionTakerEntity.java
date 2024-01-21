package com.galsie.gcs.homesdatawarehouse.data.entity.actiontaker;

import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.operators.AbstractHomeQuickActionEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class QAActionTakerEntity extends ActionTakerEntity{

    @ManyToOne
    @JoinColumn(name="associated_qa_unique_id")
    AbstractHomeQuickActionEntity qaIntermediate; // which scene is this action taker. ManyToOne since a QA might be taken many times


    @ManyToOne
    @JoinColumn(name="qa_action_taker_id")
    ActionTakerEntity initiatedBy; // an action taker could initiate multiple scene actions


}
