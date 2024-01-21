package com.galsie.gcs.homes.data.dto.invites.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeQRUserInviteResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.invites.GalsieUserAccountHomeInviteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HomeDirectUserInviteByUsernameDTO extends HomeDirectUserInviteByCommon {

    @JsonProperty("username")
    @NotNull
    private String username;

    public boolean isValidUsername() {
        if (username.length() < 3 || username.length() > 12) {
            return false;    }

        if (!username.matches("^[\\p{L}\\p{N}_\\-\\.]{3,12}$")) {
            return false;    }

        if (username.contains(" ")) {
            return false;    }

        return true;
    }

    public Optional<HomeQRUserInviteResponseErrorType> isUserByUsernameDTOValid(){
        if(!this.isValidUsername()){
            return Optional.of(HomeQRUserInviteResponseErrorType.USER_NOT_FOUND);
        }

        return Optional.empty();
    }

    public GalsieUserAccountHomeInviteEntity toGalsieUserAccountHomeInviteEntity(
            HomeEntity homeEntity, UserHomeAccessInfoDTO accessInfoDTO){

        var accessInfo = accessInfoDTO != null
                ? UserHomeAccessInfoDTO.toInviteAccessInfoEntity(accessInfoDTO)
                : null;

        return GalsieUserAccountHomeInviteEntity.builder()
                .homeEntity(homeEntity)
                .accessInfo(accessInfo)
                .build();
    }
}

