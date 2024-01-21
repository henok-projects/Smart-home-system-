package com.galsie.gcs.resources.data.dto.providiablecountriesdtos;

import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.ZoneEntityDetailedInfoDTO;
import com.galsie.gcs.resources.data.entity.countries.CityEntity;
import com.galsie.gcs.resources.data.entity.countries.ZoneEntity;

import java.util.List;

public class ZoneEntityDetailedInfoExtDTO extends ZoneEntityDetailedInfoDTO {

    public static ZoneEntityDetailedInfoDTO toDetailedZoneDTO(ZoneEntity zoneEntity, List<CityEntity> cityEntities){
        var cities = cityEntities.stream().map(CityEntityBasicInfoExtDTO::toBasicCityDTO).toList();
        var detailedZoneEntityDTO = ZoneEntityDetailedInfoDTO.detailedBuilder().cities(cities).build();
        detailedZoneEntityDTO.setId(zoneEntity.getId());
        detailedZoneEntityDTO.setName(zoneEntity.getName());
        return detailedZoneEntityDTO;
    }
}
