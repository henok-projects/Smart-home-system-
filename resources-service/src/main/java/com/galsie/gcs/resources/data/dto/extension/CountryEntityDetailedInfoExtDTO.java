package com.galsie.gcs.resources.data.dto.extension;

import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.CountryEntityDetailedInfoDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.ZoneEntityBasicInfoDTO;
import com.galsie.gcs.resources.data.entity.countries.CountryEntity;
import com.galsie.gcs.resources.data.entity.countries.ZoneEntity;

import java.util.LinkedList;
import java.util.List;

public class CountryEntityDetailedInfoExtDTO extends CountryEntityDetailedInfoDTO {

    public static CountryEntityDetailedInfoDTO toDetailedCountryDTO(CountryEntity entity, List<ZoneEntity> zoneEntities){
        List<ZoneEntityBasicInfoDTO> zones = new LinkedList<>();
        for(ZoneEntity zoneEntity: zoneEntities){
            zones.add(ZoneEntityBasicInfoExtDTO.toBasicZoneDTO(zoneEntity));
        }
        var detailedZoneEntityDTO = CountryEntityDetailedInfoDTO.builder().zones(zones).build();
        detailedZoneEntityDTO.setId(entity.getId());
        detailedZoneEntityDTO.setName(entity.getName());
        return detailedZoneEntityDTO;
    }

}
