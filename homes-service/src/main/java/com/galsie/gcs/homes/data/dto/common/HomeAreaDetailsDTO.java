package com.galsie.gcs.homes.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeAreaType;
import com.galsie.gcs.homes.data.discrete.addarea.AddHomeAreaResponseErrorType;
import com.galsie.gcs.homes.data.entity.home.area.HomeAreaDetailsEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Optional;
import java.util.regex.Pattern;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeAreaDetailsDTO {

    @JsonProperty("area_name")
    @NotNull
    private String areaName;

    @JsonProperty("initials")
    @NotNull
    private String initials;

    @JsonProperty("color")
    @NotNull
    private String initialsColor;

    @JsonProperty("area_type")
    @NotNull
    private String areaType;


    private static Pattern initialsPattern = Pattern.compile("^[A-Z]{1,3}$");

    private static Pattern areaNamepattern = Pattern.compile("[\\p{L}\\p{N}]+[\\p{L}\\p{N}_\\-.]{3,20}");


    public HomeAreaDetailsEntity toAreaDetailsEntity() {
        return HomeAreaDetailsEntity.builder()
                .initials(getInitials())
                .name(getAreaName())
                .initialsColor(getInitialsColor())
                .type(HomeAreaType.fromString(areaType).get())
                .build();
    }

    public Optional<AddHomeAreaResponseErrorType> validate(){

        if(!areaNamepattern.matcher(this.areaName).matches()){
            return Optional.of(AddHomeAreaResponseErrorType.INVALID_AREA_NAME);
        }
        if(HomeAreaType.fromString(areaType).isEmpty()){
            return Optional.of(AddHomeAreaResponseErrorType.INVALID_AREA_TYPE);
        }
        if(!initialsPattern.matcher(this.initials).matches()){
            return Optional.of(AddHomeAreaResponseErrorType.INVALID_AREA_INITIALS);
        }
        try {
            Integer.parseInt(this.initialsColor);
        }catch (NumberFormatException e){
            return Optional.of(AddHomeAreaResponseErrorType.INVALID_COLOR);
        }
        return Optional.empty();
    }
}
