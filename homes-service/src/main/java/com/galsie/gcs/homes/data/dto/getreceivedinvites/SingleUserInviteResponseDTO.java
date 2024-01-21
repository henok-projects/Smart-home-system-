package com.galsie.gcs.homes.data.dto.getreceivedinvites;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.dto.common.BasicHomeInfoDTO;
import com.galsie.gcs.homes.data.dto.common.CreationInfoDTO;
import com.galsie.gcs.homes.data.entity.home.invites.HomeInviteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SingleUserInviteResponseDTO {

    @JsonProperty("invite_id")
    @NotNull
    Long inviteId;

    @JsonProperty("invite_data")
    @NotNull
    SingleRecievedUserInviteDataDTO inviteData;

    @JsonProperty("inviting_home")
    BasicHomeInfoDTO invitingHome;

    @JsonProperty("creation_info")
    private CreationInfoDTO creationInfo;



    public static SingleUserInviteResponseDTO fromHomeInviteEntity(HomeInviteEntity homeInvite) {

        return SingleUserInviteResponseDTO.builder()
                .inviteId(homeInvite.getId())
                .inviteData(SingleRecievedUserInviteDataDTO.fromHomeInviteEntity(homeInvite))
                .invitingHome(BasicHomeInfoDTO.fromHomeEntity(homeInvite.getHomeEntity()))
                .build();
    }
}
