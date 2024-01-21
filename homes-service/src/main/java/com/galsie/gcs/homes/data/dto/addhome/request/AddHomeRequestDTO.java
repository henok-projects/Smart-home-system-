package com.galsie.gcs.homes.data.dto.addhome.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.addhome.AddHomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.HomeType;
import com.galsie.gcs.homes.data.dto.common.HomeAddressDTO;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.floor.HomeFloorEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import org.apache.commons.lang3.math.NumberUtils;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddHomeRequestDTO {


    @JsonProperty("home_name")
    @NotNull
    private String homeName;

    @JsonProperty("home_type")
    @NotNull
    private String homeType;

    @JsonProperty("home_address")
    @Nullable
    private HomeAddressDTO homeAddress;

    @JsonProperty("home_floors")
    @Nullable
    private Map<String, String> homeFloors;

    public boolean areAllFloorNumbersPositiveAndNonZero() {
        for (String floorNumber : homeFloors.keySet()) {
            try {

                Integer floorInt = NumberUtils.createInteger(floorNumber);

                if (floorInt == null || floorInt <= 0) {
                    return false;
                }
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

    public boolean isValidHomeType() {
        return EnumSet.allOf(HomeType.class)
                .stream()
                .anyMatch(e -> e.name().equalsIgnoreCase(this.homeType));
    }

    public AddHomeResponseErrorType isValidHomeName() {
        String name = this.getHomeName();
        if ( name== null) {
            return null;
        }
        if(name.isEmpty()){
            return AddHomeResponseErrorType.HOME_NAME_TOO_SHORT;
        }
        if(name.length() > 28){
            return AddHomeResponseErrorType.HOME_NAME_TOO_LONG;
        }
        String trimmedName = name.trim();

        String regex = "[\\p{L}\\p{N}]+[\\p{L}\\p{N}_\\-.\\s']*";
        return trimmedName.matches(regex) ? null : AddHomeResponseErrorType.INVALID_HOME_NAME;
    }

    public boolean isValidFloor() {
        String floorName = this.getHomeFloors().get("floor_name");
        Integer floorCount = Integer.valueOf(this.getHomeFloors().get("floor_number"));

        Pattern pattern = Pattern.compile("[\\p{L}\\p{N}]{1}[\\p{L}\\p{N}_\\-\\.]{0,18}");

        boolean isFloorNameValid = pattern.matcher(floorName).matches() && floorName.length() >= 1 && floorName.length() <= 20;

        boolean isFloorCountValid = floorCount <= 1000;

        return isFloorNameValid && isFloorCountValid;

    }

    public Optional<AddHomeResponseErrorType> validate(){
        var isValidHomeNameValue = this.isValidHomeName();
        if(isValidHomeNameValue != null){
            return Optional.of(isValidHomeNameValue);
        }

        if(!this.isValidHomeType()){
            return Optional.of(AddHomeResponseErrorType.INVALID_HOME_TYPE);
        }

        if(!this.areAllFloorNumbersPositiveAndNonZero()){
            return Optional.of(AddHomeResponseErrorType.FLOOR_NUMBER_MUST_BE_POSITIVE_NON_ZERO);
        }
        if(this.homeAddress != null && !this.homeAddress.isValidAddress()){
            return Optional.of(AddHomeResponseErrorType.INVALID_ADDRESS);
        }

        return Optional.empty();
    }

    public HomeEntity toHomeEntity() {

        List<HomeFloorEntity> homeFloors = this.getHomeFloors().entrySet().stream()
                .map(entry -> HomeFloorEntity.builder()
                        .floorNumber(Long.parseLong(entry.getKey()))
                        .floorName(entry.getValue())
                        .build()
                )
                .collect(Collectors.toList());


        var homeEntity = HomeEntity.builder()
                .name(this.getHomeName())
                .homeFloors(homeFloors)
                .address(homeAddress.toHomeAddressEntity())
                .type(HomeType.valueOf(this.getHomeType().toUpperCase()))
                .build();

        for(HomeFloorEntity homeFloor: homeFloors){
            homeFloor.setHome(homeEntity);
        }

        var address = homeAddress.toHomeAddressEntity();
        address.setHome(homeEntity);
        homeEntity.setAddress(address);
        homeEntity.setHomeFloors(homeEntity.getHomeFloors());

        return homeEntity;
    }

}

