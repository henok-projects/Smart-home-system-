package com.galsie.gcs.homes.data.dto.invites.response.qr;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class HomeQRUserInviteJoinedUserDTO {
    @JsonProperty("username")
    @NotNull
    private String username;

    @JsonProperty("full_name")
    @NotNull
    private String fullName;

    @JsonProperty("image")
    @Nullable
    private String base64EncodedImage;

    @JsonProperty("join_date")
    @NotNull
    private String joinDate;

}
