package com.galsie.gcs.homes.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.address.HomeAddressEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import reactor.util.annotation.Nullable;

import java.util.regex.Pattern;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeAddressDTO {

    @JsonProperty("country_id")
    @NotNull
    private Long countryId;

    @JsonProperty("zone_id")
    @NotNull
    private Long zoneId;

    @JsonProperty("city_id")
    @NotNull
    private Long cityId;

    @JsonProperty("post_code")
    @NotNull
    private String postCode;

    @JsonProperty("address_line1")
    @NotNull
    private String addressLine1;

    @JsonProperty("address_line2")
    @Nullable
    private String addressLine2;

    @JsonProperty("longitude")
    @Nullable
    private Double longitude;

    @JsonProperty("latitude")
    @Nullable
    private Double latitude;


    public static Pattern pattern = Pattern.compile("[\\p{L}\\p{N}_.\\-@&(),\\s]{1,50}");

    public HomeAddressEntity toHomeAddressEntity() {

        return HomeAddressEntity.builder()
                .countryId(countryId)
                .zoneId(zoneId)
                .cityId(cityId)
                .postCode(postCode)
                .addressLine1(addressLine1)
                .addressLine2(addressLine2)
                .longitude(longitude)
                .latitude(latitude)
                .build();

    }

    public static HomeAddressDTO fromHomeAddressEntity(HomeEntity homeEntity){
        var address = homeEntity.getAddress();
        return HomeAddressDTO.builder()
                .countryId(address.getCountryId())
                .zoneId(address.getZoneId())
                .cityId(address.getCityId())
                .postCode(address.getPostCode())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
    }

    public boolean isValidAddress() {
        return isValidAddressLine1() && isValidAddressLine2();
    }

    public boolean isValidAddressLine1() {
        return pattern.matcher(this.addressLine1).matches();
    }

    public boolean isValidAddressLine2() {
        if(addressLine2 == null){
            return true;
        }
        return pattern.matcher(addressLine2).matches();
    }

}