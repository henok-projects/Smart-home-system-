package com.galsie.gcs.homes.data.entity.home.invites;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeDirectUserSetInviteEntity extends HomeInviteEntity implements GalEntity<Long> {

    @OneToMany
    List<HomeDirectUserInviteEntity> directUserInviteUserEntities;
}
