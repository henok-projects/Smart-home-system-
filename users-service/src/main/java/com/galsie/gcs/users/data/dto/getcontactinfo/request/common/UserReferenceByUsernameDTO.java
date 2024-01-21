package com.galsie.gcs.users.data.dto.getcontactinfo.request.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserReferenceByUsernameDTO implements UserReferenceDTO {

    @JsonProperty("username")
    @NotNull
    private String username;

}
