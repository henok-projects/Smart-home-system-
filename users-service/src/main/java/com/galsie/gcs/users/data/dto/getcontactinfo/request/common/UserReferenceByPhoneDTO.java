package com.galsie.gcs.users.data.dto.getcontactinfo.request.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Builder
public class UserReferenceByPhoneDTO implements UserReferenceDTO {

    @JsonProperty("country_code")
    @NotNull
    private short countryCode;

    @JsonProperty("phone")
    @NotNull
    private String phone;
}
