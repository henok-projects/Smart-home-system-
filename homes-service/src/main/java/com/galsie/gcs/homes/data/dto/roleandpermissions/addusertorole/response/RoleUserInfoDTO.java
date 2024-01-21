package com.galsie.gcs.homes.data.dto.roleandpermissions.addusertorole.response;

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
public class RoleUserInfoDTO {

    @JsonProperty("role_id")
    @NotNull
    private Long roleId;

    @JsonProperty("with_permissions")
    @NotNull
    private List<String> withPermissions;

    @JsonProperty("without_permissions")
    @NotNull
    private List<String> withoutPermissions;

}

