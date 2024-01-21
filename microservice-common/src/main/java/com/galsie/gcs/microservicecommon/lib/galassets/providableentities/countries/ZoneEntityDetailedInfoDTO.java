package com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(builderMethodName = "detailedBuilder")
public class ZoneEntityDetailedInfoDTO extends ZoneEntityBasicInfoDTO{

    @NotNull
    private List<CityEntityBasicInfoDTO> cities;

}
