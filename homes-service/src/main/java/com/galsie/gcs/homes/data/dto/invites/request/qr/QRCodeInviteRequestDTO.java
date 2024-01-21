package com.galsie.gcs.homes.data.dto.invites.request.qr;



import com.galsie.gcs.homes.data.discrete.homeinvite.HomeQRUserInviteStatus;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.dto.invites.request.AbstractHomeUserInviteRequestDTO;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.invites.QRBasedHomeInviteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;

@GalDTO
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QRCodeInviteRequestDTO extends AbstractHomeUserInviteRequestDTO {

    public QRBasedHomeInviteEntity toQRBasedHomeInviteEntity(HomeEntity homeEntity){
        return QRBasedHomeInviteEntity.builder()
                .homeEntity(homeEntity)
                .userHomeAccessInfoEntity(UserHomeAccessInfoDTO.toInviteAccessInfoEntity(this.getAccessInfo()))
                .homeQRUserInviteStatus(HomeQRUserInviteStatus.ACTIVE)
                .build();
    }
}
