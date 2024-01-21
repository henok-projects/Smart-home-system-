package com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CityEntityBasicInfoDTO {

    @NotNull
    private long id;

    @NotNull
    private String name;


}
