package com.galsie.gcs.homes.data.dto.invites.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeDirectUserInviteStatus;
import com.galsie.gcs.homes.data.dto.common.PhoneNumberDTO;
import com.galsie.gcs.homes.data.entity.home.invites.EmailOrPhoneBasedHomeInviteEntity;
import com.galsie.gcs.homes.data.entity.home.invites.GalsieUserAccountHomeInviteEntity;
import com.galsie.gcs.homes.data.entity.home.invites.HomeInviteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import reactor.util.annotation.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeDirectUserInviteUserInfoDTO {

    @JsonProperty("full_name")
    @Nullable
    private String fullName;

    @JsonProperty("user_name")
    @Nullable
    private String username;

    @JsonProperty("email")
    @Nullable
    private String email;

    @JsonProperty("phone_number")
    @Nullable
    private PhoneNumberDTO phoneNumber;

    @JsonProperty("invite_status")
    @NotNull
    private HomeDirectUserInviteStatus inviteStatus;

    @JsonProperty("user_action_date")
    @Nullable
    private String userActionDate;

    public static HomeDirectUserInviteUserInfoDTO fromInviteEntity(HomeInviteEntity homeInviteEntity) {

        if (homeInviteEntity instanceof EmailOrPhoneBasedHomeInviteEntity emailOrPhoneBasedHomeInviteEntity) {
            return HomeDirectUserInviteUserInfoDTO.builder()
                    .email(emailOrPhoneBasedHomeInviteEntity.getEmail())
                    .fullName(emailOrPhoneBasedHomeInviteEntity.getFullName())
                    .phoneNumber(PhoneNumberDTO.fromEntity(((EmailOrPhoneBasedHomeInviteEntity) homeInviteEntity).getPhoneNumber()))
                    .inviteStatus(HomeDirectUserInviteStatus.ACTIVE)
                    .build();
        } else if (homeInviteEntity instanceof GalsieUserAccountHomeInviteEntity galsieUserAccountHomeInviteEntity) {
            return HomeDirectUserInviteUserInfoDTO.builder()
                    .inviteStatus(HomeDirectUserInviteStatus.ACTIVE)
                    .userActionDate(String.valueOf(galsieUserAccountHomeInviteEntity.getCreatedAt()))
                    .build();
        }

        return null;
    }

}
