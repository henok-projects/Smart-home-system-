package com.galsie.gcs.resources.data.dto.providiablecountriesdtos;

import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.CityEntityBasicInfoDTO;
import com.galsie.gcs.resources.data.entity.countries.CityEntity;

public class CityEntityBasicInfoExtDTO extends CityEntityBasicInfoDTO {


    public static CityEntityBasicInfoDTO toBasicCityDTO(CityEntity entity){
        return CityEntityBasicInfoDTO.builder().id(entity.getId()).name(entity.getName()).build();
    }

}
