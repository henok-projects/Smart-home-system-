package com.galsie.gcs.homes.data.dto.invites.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.dto.invites.request.AbstractHomeUserInviteRequestDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeDirectUserSetInviteRequestDTO extends AbstractHomeUserInviteRequestDTO {

    @JsonProperty("invite_list")
    @NotNull
    private List<HomeDirectUserInviteByCommon> inviteList;

}

