package com.galsie.gcs.homes.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import reactor.util.annotation.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AreaBasicInfoDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("area_details")
    @NotNull
    private HomeAreaDetailsDTO areaDetailsDTO;

    @JsonProperty("floor")
    @Nullable
    private Long floorId;

    @JsonProperty("area_id")
    @NotNull
    private Long areaId;

}
