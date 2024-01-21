package com.galsie.gcs.homes.data.dto.roleandpermissions.addrole.request;

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
public class AddHomeRoleRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("role_name")
    @NotNull
    private String roleName;

    @JsonProperty("role_icon_image_reference")
    @NotNull
    private String roleIconImageReference;

    @JsonProperty("similar_preset_role_id")
    @NotNull
    private Long similarPresetRoleId;

    public boolean isValidHomeId() {
        return this.homeId != null && this.homeId >+ 0;
    }


}

