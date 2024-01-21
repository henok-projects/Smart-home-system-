package com.galsie.gcs.homes.data.dto.roleandpermissions.editroleidentity.request;


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
public class EditHomeRoleIdentityRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("role_id")
    @NotNull
    private Long roleId;

    @JsonProperty("role_name")
    @NotNull
    private String roleName;

    @JsonProperty("role_icon_image_reference")
    @NotNull
    private String roleIconImageReference;

    public boolean isValidRoleId() {
        return this.roleId != null && this.roleId >= 0;
    }

    public boolean isValidHomeId() {
        return this.homeId != null && this.homeId >= 0;
    }

}

