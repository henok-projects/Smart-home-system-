package com.galsie.gcs.homes.data.dto.invites.response.qr;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeQRUserInviteStatus;
import com.galsie.gcs.homes.data.dto.common.CreationInfoDTO;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.entity.home.invites.*;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeQRUserInviteDataDTO {

    @JsonProperty("invite_id")
    @NotNull
    private long inviteId;

    @JsonProperty("invite_unique_code")
    @NotNull
    private String inviteUniqueCode;

    @JsonProperty("creation_info")
    @NotNull
    private CreationInfoDTO creationInfo;

    @JsonProperty("user_access_info")
    @NotNull
    private UserHomeAccessInfoDTO userAccessInfo;

    @JsonProperty("invite_status")
    @NotNull
    private HomeQRUserInviteStatus inviteStatus;

    @JsonProperty("users_joined")
    @Nullable
    private List<HomeQRUserInviteJoinedUserDTO> usersJoined;

    public static HomeQRUserInviteDataDTO fromQRCodeHomeInviteDataEntity(HomeInviteEntity homeInviteEntity) {

        return HomeQRUserInviteDataDTO.builder()
                .inviteId(homeInviteEntity.getId())
                .inviteUniqueCode(homeInviteEntity.inviteUniqueCode)
                .userAccessInfo(UserHomeAccessInfoDTO.fromInviteAccessInfoEntity(homeInviteEntity.getAccessInfo()))
                .inviteStatus(HomeQRUserInviteStatus.ACTIVE)
                .build();

    }

}
