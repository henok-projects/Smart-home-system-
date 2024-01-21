package com.galsie.gcs.homes.data.entity.home.invites;

import com.galsie.gcs.homes.data.constant.HomeUserInviteType;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeQRUserInviteStatus;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue(HomeUserInviteType.QR_BASED)
public class QRBasedHomeInviteEntity extends HomeInviteEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="home_user_invite_id", nullable = false)
    Long id;

    @Column(name = "invite_status")
    private HomeQRUserInviteStatus homeQRUserInviteStatus;

    @Builder
    public QRBasedHomeInviteEntity(Long id, HomeEntity homeEntity, HomeInviteAccessInfoEntity userHomeAccessInfoEntity, HomeUserEntity user, String inviteUniqueCode, Long inviteId, String inviteUniqueCode1, HomeInviteAccessInfoEntity userAccessInfo, String inviteValidUntil, HomeQRUserInviteStatus homeQRUserInviteStatus) {
        super(id, homeEntity, userHomeAccessInfoEntity, user, inviteUniqueCode);
        this.inviteUniqueCode = inviteUniqueCode1;
        this.homeQRUserInviteStatus = homeQRUserInviteStatus;
    }
}
