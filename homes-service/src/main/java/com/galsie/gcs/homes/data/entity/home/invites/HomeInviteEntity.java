package com.galsie.gcs.homes.data.entity.home.invites;

import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "invite_type",  discriminatorType = DiscriminatorType.STRING)
public abstract class HomeInviteEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="home_id")
    HomeEntity homeEntity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    HomeInviteAccessInfoEntity accessInfo;

    @ManyToOne // one invitor can have many invitations sent
    HomeUserEntity invitingUser; // the inviting user

    @Column(name = "invite_unique_code")
    public String inviteUniqueCode;

}
