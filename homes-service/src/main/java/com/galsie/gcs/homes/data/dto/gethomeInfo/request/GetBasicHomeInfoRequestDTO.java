package com.galsie.gcs.homes.data.dto.gethomeInfo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetBasicHomeInfoRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;


    public boolean isValidHomeId() {
        return this.homeId > 0;
    }


    public Optional<HomeResponseErrorType> validate(){
        if(!this.isValidHomeId()){
            return Optional.of(HomeResponseErrorType.INVALID_HOME_ID);
        }

        return Optional.empty();
    }

}