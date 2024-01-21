package com.galsie.gcs.homes.data.dto.roleandpermissions.deleterolefromhome.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeleteHomeRoleRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("role_id")
    @NotNull
    private Long roleId;


    public boolean isValidRoleId() {
        return this.roleId != null && this.roleId >+ 0;
    }

    public boolean isValidHomeId() {
        return this.homeId != null && this.homeId >+ 0;
    }


}

