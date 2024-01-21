package com.galsie.gcs.homes.data.dto.invites.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.dto.common.CreationInfoDTO;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.entity.home.invites.HomeInviteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
/**

 Note: Part of {@link .....HomeUserInviteResponseDTO)
 */
public class HomeDirectUserSetInviteDataDTO {

    @JsonProperty("invite_id")
    @NotNull
    private Long inviteId;

    @JsonProperty("invite_unique_code")
    @Nullable
    private String inviteUniqueCode;

    @JsonProperty("creation_info")
    @Nullable
    private CreationInfoDTO creationInfo;

    @JsonProperty("access_info")
    @Nullable
    private UserHomeAccessInfoDTO userAccessInfo;

    @JsonProperty("invite_list")
    @Nullable
    private List<HomeDirectUserInviteDataDTO> inviteList;


    private static List<HomeDirectUserInviteDataDTO> convertInviteEntitiesToDTOs(List<HomeInviteEntity> inviteEntities) {
        return inviteEntities.stream()
                .map(inviteEntity -> {
                    HomeDirectUserInviteUserInfoDTO userInfo = HomeDirectUserInviteUserInfoDTO.fromInviteEntity(inviteEntity);
                    return HomeDirectUserInviteDataDTO.builder()
                            .homeDirectUserInviteUserInfoDTO(userInfo)
                            .build();
                })
                .collect(Collectors.toList());
    }



    public static HomeDirectUserSetInviteDataDTO fromHomeInviteEntity(HomeInviteEntity homeInviteEntity){
        List<HomeInviteEntity> inviteEntities = new ArrayList<>();
        inviteEntities.add(homeInviteEntity);
        List<HomeDirectUserInviteDataDTO> inviteList = convertInviteEntitiesToDTOs(inviteEntities);

        return HomeDirectUserSetInviteDataDTO.builder()
                .inviteId(homeInviteEntity.getId())
                .inviteUniqueCode(homeInviteEntity.getInviteUniqueCode())
                .userAccessInfo(UserHomeAccessInfoDTO.fromInviteAccessInfoEntity(homeInviteEntity.getAccessInfo()))
                .inviteList(inviteList)
                .build();
    }

}
