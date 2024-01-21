package com.galsie.gcs.homes.data.dto.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.entity.home.common.PhoneNumberEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PhoneNumberDTO {

    @JsonProperty("country_code")
    @NotNull
    private String countryCode;

    @JsonProperty("phone")
    @NotNull
    private String phone;


    public static PhoneNumberDTO fromEntity(PhoneNumberEntity entity) {
        if (entity == null) {
            return null;
        }
        return PhoneNumberDTO.builder()
                .countryCode(String.valueOf(entity.getCountryCode()))
                .phone(entity.getPhone())
                .build();
    }

    public static PhoneNumberEntity toPhoneNumberEntity(PhoneNumberDTO phoneNumberDTO) {

        return PhoneNumberEntity.builder()
                .countryCode(phoneNumberDTO.getCountryCode())
                .phone(phoneNumberDTO.getPhone())
                .build();
    }


}

