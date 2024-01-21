package com.galsie.gcs.homes.data.dto.addarea.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.addarea.AddHomeAreaResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.HomeAreaDetailsDTO;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.area.HomeAreaEntity;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.areaconfig.AreaConfigurationProvidedAssetDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.utils.pair.Pair;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Optional;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddHomeAreaRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("area_details")
    @NotNull
    private HomeAreaDetailsDTO areaDetails;

    @JsonProperty("floor_id")
    @Nullable
    private Long floorId;


    public Optional<AddHomeAreaResponseErrorType> validate(){
        var areaDetailsValidation = this.areaDetails.validate();
        if(areaDetailsValidation.isPresent()){
            return areaDetailsValidation;
        }
        if(floorId != null && floorId < 0){
            return Optional.of(AddHomeAreaResponseErrorType.INVALID_FLOOR);
        }
        return Optional.empty();
    }


    public  HomeAreaEntity toHomeAreaEntity(HomeEntity homeEntity, AreaConfigurationProvidedAssetDTO areaConfigurationProvidedAssetDTO){
        var homeArea = HomeAreaEntity.builder()
                .home(homeEntity)
                .doors(new ArrayList<>())
                .windows(new ArrayList<>())
                .build();
        if(this.floorId != null){
            var matchHomeFloorOpt = homeEntity.getHomeFloors().stream().filter(floor -> floor.getFloorNumber().equals(this.floorId)).findFirst();
            homeArea.setFloorOfArea(matchHomeFloorOpt.get());
        }
        var areaDetail = this.areaDetails.toAreaDetailsEntity();
        areaDetail.setHomeAreaEntity(homeArea);
        homeArea.setAreaDetailsEntity(areaDetail);
        return homeArea;
    }
}
