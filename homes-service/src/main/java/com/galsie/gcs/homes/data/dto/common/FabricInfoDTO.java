package com.galsie.gcs.homes.data.dto.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
class FabricInfoDTO {


    @JsonProperty("fabric_id")
    @Nullable
    private Long fabricId;

    @JsonProperty("ipk")
    @Nullable
    private String ipk;
}
