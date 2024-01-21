package com.galsie.gcs.homes.data.dto.invites.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeDirectUserInviteResponseErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeDirectUserInviteDataDTO {

    @JsonProperty("invited_user_response_error")
    @Nullable
    HomeDirectUserInviteResponseErrorType homeDirectUserInviteResponseErrorType;

    @JsonProperty("invited_user_info")
    @NotNull
    HomeDirectUserInviteUserInfoDTO homeDirectUserInviteUserInfoDTO;

    public static HomeDirectUserInviteDataDTO error(HomeDirectUserInviteResponseErrorType homeDirectUserInviteResponseErrorType){
        return new HomeDirectUserInviteDataDTO(homeDirectUserInviteResponseErrorType, null);
    }

    public static HomeDirectUserInviteDataDTO success(HomeDirectUserInviteUserInfoDTO homeDirectUserInviteUserInfoDTO){
        return new HomeDirectUserInviteDataDTO(null, homeDirectUserInviteUserInfoDTO);
    }

}
