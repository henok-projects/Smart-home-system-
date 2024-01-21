package com.galsie.gcs.homes.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AreaInfoDTO extends AreaBasicInfoDTO{


    @JsonProperty("doors")
    @NotNull
    private List<DoorDTO> doors;

    @JsonProperty("windows")
    @NotNull
    private List<WindowDTO> windows;


}
