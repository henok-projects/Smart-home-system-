package com.galsie.gcs.homes.data.entity.home.user;

import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.homes.data.entity.home.user.preferences.HomeUserPreferencesEntity;
import com.galsie.gcs.homescommondata.data.entity.home.user.AbstractHomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/*
An app user associated with a home
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeUserEntity extends AbstractHomeUserEntity implements GalEntity<Long> {

    @OneToOne(mappedBy = "homeUser")
    HomeUserPreferencesEntity homeUserPreferences;

    @OneToOne(mappedBy = "homeUserEntity",cascade = CascadeType.ALL)
    HomeUserAccessInfoEntity homeUserAccessInfoEntity;

    @OneToMany
    List<HomeRoleEntity> homeRoleEntity;

    @Column(name="joined_at")
    @CreationTimestamp
    LocalDateTime joinedAt;

    @Column(columnDefinition = "boolean default false")
    private boolean homeArchivedForUser;

    @PrePersist
    public void onCreate(){
        /*
        TODO: Ensure that the newly created entity isn't a duplicate (by ensuring that user doesn't already have access to this home))
         */
    }

    public  Long getUserId(){
        return  this.getAppUser().getAppUserId();
    }


    public HomeEntity getHomeEntity() {
        return (HomeEntity) this.getHome();
    }

}
