package com.galsie.gcs.homes.data.dto.invites.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeQRUserInviteResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.dto.invites.request.qr.QRCodeInviteRequestDTO;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserSetInviteRequestDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@ABType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HomeDirectUserSetInviteRequestDTO.class, name = "homeDirectUserInvite"),
        @JsonSubTypes.Type(value = QRCodeInviteRequestDTO.class, name = "inviteUserByQRCodeRequestDTO"),
})
public abstract class AbstractHomeUserInviteRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("access_info")
    @NotNull
    private UserHomeAccessInfoDTO accessInfo;

    public boolean isValidHomeId() {
        return this.homeId >= 0;
    }

    public Optional<HomeQRUserInviteResponseErrorType> validate() {
        if (!this.isValidHomeId()) {
            return Optional.of(HomeQRUserInviteResponseErrorType.USER_NOT_FOUND);
        }

        return Optional.empty();
    }

}