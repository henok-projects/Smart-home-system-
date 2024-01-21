package com.galsie.gcs.homes.data.dto.getreceivedinvites;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.dto.invites.response.user.HomeDirectUserInviteUserInfoDTO;
import com.galsie.gcs.homes.data.entity.home.invites.HomeInviteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SingleRecievedUserInviteDataDTO {

    @JsonProperty("invite_id")
    @NotNull
    private Long inviteId;

    @JsonProperty("invited_user_info")
    @Nullable
    private HomeDirectUserInviteUserInfoDTO homeDirectUserInviteUserInfoDTO;

    @JsonProperty("invite_unique_code")
    @Nullable
    private String inviteUniqueCode;

    @JsonProperty("access_info")
    @Nullable
    private UserHomeAccessInfoDTO userAccessInfo;

    @JsonProperty("invite_valid_until")
    @Nullable
    private LocalDateTime InviteValidUntil;

    public static SingleRecievedUserInviteDataDTO fromHomeInviteEntity(HomeInviteEntity homeDirectUserInviteEntity){

        return SingleRecievedUserInviteDataDTO.builder()
                .inviteId(homeDirectUserInviteEntity.getId())
                .inviteUniqueCode(homeDirectUserInviteEntity.inviteUniqueCode)
                .homeDirectUserInviteUserInfoDTO(HomeDirectUserInviteUserInfoDTO.fromInviteEntity(homeDirectUserInviteEntity))
                .userAccessInfo(UserHomeAccessInfoDTO.fromInviteAccessInfoEntity(homeDirectUserInviteEntity.getAccessInfo()))
                .build();
    }

}
