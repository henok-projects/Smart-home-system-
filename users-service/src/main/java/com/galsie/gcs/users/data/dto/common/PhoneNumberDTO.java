package com.galsie.gcs.users.data.dto.common;

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
public class PhoneNumberDTO {

    @JsonProperty("country_code")
    @NotNull
    private String countryCode;

    @JsonProperty("number")
    @NotNull
    private String number;
}
