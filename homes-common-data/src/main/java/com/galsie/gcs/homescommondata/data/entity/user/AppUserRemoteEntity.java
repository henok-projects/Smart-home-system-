package com.galsie.gcs.homescommondata.data.entity.user;

import com.galsie.gcs.homescommondata.data.entity.home.user.AbstractHomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/*
App User from users service
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//	new code
public class AppUserRemoteEntity implements GalEntity<Long> {

    @Id
    @Column(name = "appUserId", nullable = false)
    Long appUserId; // same id as in users service

    @OneToMany(mappedBy = "appUser",cascade = CascadeType.ALL)
    List<AbstractHomeUserEntity> homeUsers;

}
