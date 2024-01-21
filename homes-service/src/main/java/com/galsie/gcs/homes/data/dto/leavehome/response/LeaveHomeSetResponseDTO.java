package com.galsie.gcs.homes.data.dto.leavehome.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LeaveHomeSetResponseDTO {

    @JsonProperty("leave_home_responses")
    @NotNull
    private List<LeaveSingleHomeResponseDTO> leaveHomeResponses;


    public static LeaveHomeSetResponseDTO fromHomeEntity(List<HomeEntity> homeEntityList){

        List<LeaveSingleHomeResponseDTO> leaveHomes = new ArrayList<>();
        homeEntityList.stream().forEach(homeEntity -> {
            var homeLeave = LeaveSingleHomeResponseDTO.builder()
                    .homeId(homeEntity.getHomeId())
                    .build();
            leaveHomes.add(homeLeave);
        });

        return LeaveHomeSetResponseDTO.builder()
                .leaveHomeResponses(leaveHomes)
                .build();
    }

}
