package com.galsie.gcs.homesdatawarehouse.data.entity.actiontaker;

import com.galsie.gcs.homescommondata.data.entity.serviceintermediate.operators.AbstractHomeAutomationEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class AutomationActionTakerEntity extends ActionTakerEntity{
    @ManyToOne
    @JoinColumn(name="associated_automation_unique_id")
    AbstractHomeAutomationEntity automationIntermediate; // which automation is this action taker. ManyToOne since an automation will run many times

}
