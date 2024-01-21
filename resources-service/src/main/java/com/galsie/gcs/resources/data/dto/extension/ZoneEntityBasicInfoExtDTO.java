package com.galsie.gcs.resources.data.dto.extension;

import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.ZoneEntityBasicInfoDTO;
import com.galsie.gcs.resources.data.entity.countries.ZoneEntity;

public class ZoneEntityBasicInfoExtDTO extends ZoneEntityBasicInfoDTO {

    public static ZoneEntityBasicInfoDTO toBasicZoneDTO(ZoneEntity entity){
        return ZoneEntityBasicInfoDTO.basicBuilder().id(entity.getId()).name(entity.getName()).build();
    }

}
