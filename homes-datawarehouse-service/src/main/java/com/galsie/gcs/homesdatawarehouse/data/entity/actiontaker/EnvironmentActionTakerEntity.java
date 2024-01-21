package com.galsie.gcs.homesdatawarehouse.data.entity.actiontaker;

import javax.persistence.Entity;

/*
An EnvironmentActionTaker takes actions through changes that happen in the environment. It has no other ActionTaker since its the environment itself.

An example of an action taken by the EnvironmentActionTaker is a change in temperature
 */
@Entity
public class EnvironmentActionTakerEntity extends ActionTakerEntity {

}
