package com.galsie.gcs.homes.data.dto.roleandpermissions.setcategoryhomerolepermissions.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SetSubCategoryHomeRolePermissionRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("role_id")
    @NotNull
    private Long roleId;

    @JsonProperty("category_key")
    @NotNull
    private String categoryKey;

    @JsonProperty("sub_category_key")
    @NotNull
    private String subCategoryKey;

    @JsonProperty("with_permissions")
    @NotNull
    private List<String> withPermissions;

    @JsonProperty("without_permissions")
    @NotNull
    private List<String> withoutPermissions;


    public boolean isValidRoleId() {
        return this.roleId != null && this.roleId >= 0;
    }

    public boolean isValidHomeId() {
        return this.homeId != null && this.homeId >= 0;
    }

}
