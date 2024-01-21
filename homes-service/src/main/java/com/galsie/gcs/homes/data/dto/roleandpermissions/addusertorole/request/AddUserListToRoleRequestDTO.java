package com.galsie.gcs.homes.data.dto.roleandpermissions.addusertorole.request;


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
public class AddUserListToRoleRequestDTO {

    @NotNull
    @JsonProperty("home_id")
    private Long homeId;

    @NotNull
    @JsonProperty("role_id")
    private Long roleId;

    @NotNull
    @JsonProperty("home_user_ids")
    private List<Long> homeUserIds;

    public boolean isValidRoleId() {
        return this.roleId != null && this.roleId >= 0;
    }

    public boolean isValidHomeId() {
        return this.homeId != null && this.homeId >= 0;
    }

}

