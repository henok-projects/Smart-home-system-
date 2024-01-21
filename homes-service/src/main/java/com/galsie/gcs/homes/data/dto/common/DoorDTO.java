package com.galsie.gcs.homes.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.entity.home.area.HomeAreaEntity;
import com.galsie.gcs.homes.data.entity.home.door.HomeDoorEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.utils.lang.NotNull;
import com.galsie.lib.utils.lang.Nullable;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DoorDTO {
    //this is the area id it connects to

    @JsonProperty("door_id")
    @NotNull
    private Long doorId;

    @JsonProperty("area_id")
    @NotNull
    private Long areaId;

    @JsonProperty("connects_to area")
    @Nullable
    private Long connectsTo;

    @JsonProperty("door_name")
    @NotNull
    private String doorName;




    public static DoorDTO fromEntity(HomeDoorEntity homeDoorEntity) {
        return DoorDTO.builder()
                .doorId(homeDoorEntity.getId())
                .areaId(homeDoorEntity.getConnectsFromArea().getId())
                .connectsTo(homeDoorEntity.getConnectsToArea())
                .doorName(homeDoorEntity.getDoorName())
                .build();
    }

    public static List<HomeDoorEntity> toDoorEntityList(List<DoorDTO> doorDTOList) {
        return doorDTOList.stream()
                .map(doorDTO -> HomeDoorEntity.builder()
                        .connectsToArea(doorDTO.getConnectsTo())
                        .doorName(doorDTO.getDoorName())
                        .build())
                .collect(Collectors.toList());
    }

    public static List<DoorDTO> toDoorDTOList(HomeAreaEntity homeAreaEntity) {
        return homeAreaEntity.getDoors().stream()
                .map(homeDoorEntity -> DoorDTO.builder()
                        .connectsTo(homeDoorEntity.getConnectsToArea())
                        .doorName(homeDoorEntity.getDoorName())
                        .build())
                .collect(Collectors.toList());
    }

}
