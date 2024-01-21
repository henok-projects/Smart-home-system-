package com.galsie.gcs.homesdatawarehouse.data.entity.actiontaker;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/*
An ActionTaker can be a User, Assistant, Scene, Quick Action, Environment, unknown..
An Action Taker is an basically entity associated with a set of actions described by HomeDeviceAttributeDataPoint in homes-datawarehouse-service.

 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // actionTaker is a table and its subclasses are tables which are primary key joined by unique id to ActionTakerEntity
@Getter
public abstract class ActionTakerEntity implements GalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="uniqueId")
    Long uniqueId;

    @Column(name="actionsTakenAt")
    LocalDateTime actionsTakenAt;

    @OneToMany(mappedBy = "actionTaker")
    List<HomeDeviceAttributeDataPointEntity> actionAttributeData; // the attribute data points associated with this action

}
