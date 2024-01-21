package com.galsie.gcs.homes.data.entity.home.invites;

import com.galsie.gcs.homes.data.constant.HomeUserInviteType;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue(HomeUserInviteType.GALSIE_ACCOUNT)
public class GalsieUserAccountHomeInviteEntity extends HomeDirectUserInviteEntity implements GalEntity<Long> {

    @Column(name="invited_user_id")
    Long invitedUserid; // foreign key from User service

    @Column(name="expires_at", nullable = true) // if null, never expires
    LocalDateTime expiresAt;

    @Column(name="created_at")
    LocalDateTime createdAt;

    @Column(name="modified_at")
    LocalDateTime modifiedAt;

    @Builder
    public GalsieUserAccountHomeInviteEntity(Long id, HomeEntity homeEntity, HomeInviteAccessInfoEntity accessInfo,
                                             String inviteUniqueCode, Long invitedUserid) {
        super();
        this.id = id;
        this.invitedUserid = invitedUserid;

    }

}
