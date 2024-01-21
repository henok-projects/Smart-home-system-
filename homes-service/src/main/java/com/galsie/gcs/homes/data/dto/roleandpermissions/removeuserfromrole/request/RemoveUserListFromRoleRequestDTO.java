package com.galsie.gcs.homes.data.dto.roleandpermissions.removeuserfromrole.request;

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
public class RemoveUserListFromRoleRequestDTO {

    @JsonProperty("role_id")
    @NotNull
    private Long roleId;

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

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

