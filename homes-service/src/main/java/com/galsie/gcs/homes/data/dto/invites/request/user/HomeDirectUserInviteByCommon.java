package com.galsie.gcs.homes.data.dto.invites.request.user;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.Getter;
import lombok.Setter;

@GalDTO
@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@HDType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HomeDirectUserInviteByEmailOrPhoneDTO.class, name = "inviteUserByEmailOrPhone"),
        @JsonSubTypes.Type(value = HomeDirectUserInviteByUsernameDTO.class, name = "inviteUserUsername"),
})
public abstract class HomeDirectUserInviteByCommon {

}
