package com.galsie.gcs.homes.data.dto.deletehome.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.deletehome.DeleteHomeResponseErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import reactor.util.annotation.Nullable;

import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeleteHomeRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("force_operation")
    @Nullable
    private Boolean forceOperation;


    public boolean isValidHomeId() {
        return this.homeId > 0;
    }

    public Optional<DeleteHomeResponseErrorType> validate(){
        if(!this.isValidHomeId()){
            return Optional.of(DeleteHomeResponseErrorType.HOME_NOT_VALID);
        }

        return Optional.empty();
    }
    public boolean isForceOperation() {
        return this.forceOperation != null && this.forceOperation;
    }

}
