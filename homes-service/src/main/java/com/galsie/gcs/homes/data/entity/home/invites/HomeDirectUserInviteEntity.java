package com.galsie.gcs.homes.data.entity.home.invites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// its part of HomeDirectUserSetInviteEntity
public abstract class HomeDirectUserInviteEntity extends HomeInviteEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id", nullable = false)
    Long id;

}
