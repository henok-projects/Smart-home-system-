package com.galsie.gcs.homes.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeType;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
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
public class BasicHomeInfoDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("home_name")
    @NotNull
    private String homeName;

    @JsonProperty("home_type")
    @NotNull
    private HomeType homeType;

    @JsonProperty("home_address")
    @Nullable
    private HomeAddressDTO homeAddress;

    public static BasicHomeInfoDTO fromHomeEntity(HomeEntity homeEntity) {
        return BasicHomeInfoDTO.builder()
                .homeId(homeEntity.getHomeId())
                .homeName(homeEntity.getName())
                .homeAddress(HomeAddressDTO.fromHomeAddressEntity(homeEntity))
                .homeType(homeEntity.getType())
                .build();

    }

}
