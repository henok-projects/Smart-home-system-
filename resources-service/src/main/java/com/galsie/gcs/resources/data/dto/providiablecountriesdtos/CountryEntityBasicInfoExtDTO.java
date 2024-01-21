package com.galsie.gcs.resources.data.dto.providiablecountriesdtos;

import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.CountryEntityBasicInfoDTO;
import com.galsie.gcs.resources.data.entity.countries.CountryEntity;

public class CountryEntityBasicInfoExtDTO extends CountryEntityBasicInfoDTO {

    public static CountryEntityBasicInfoDTO toBasicCountryDTO(CountryEntity entity){
        return CountryEntityBasicInfoDTO.basicBuilder().id(entity.getId()).name(entity.getName())
                .zipCodePattern(entity.getZipcodePattern() == null ? "^[\\p{L}\\p{N}]{2,10}$" : entity.getZipcodePattern()).build();
    }

}
