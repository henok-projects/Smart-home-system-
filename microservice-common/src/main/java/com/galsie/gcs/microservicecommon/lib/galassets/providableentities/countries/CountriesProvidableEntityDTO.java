package com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CountriesProvidableEntityDTO{

    @NotNull
    List<CountryEntityBasicInfoDTO> countries;

}
