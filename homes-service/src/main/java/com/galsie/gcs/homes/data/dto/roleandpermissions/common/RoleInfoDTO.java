package com.galsie.gcs.homes.data.dto.roleandpermissions.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
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
public class RoleInfoDTO {

    @JsonProperty("role_name")
    @NotNull
    private String roleName;

    @JsonProperty("role_icon_image_reference")
    @NotNull
    private String roleIconImageReference;

    @JsonProperty("with_permissions")
    @NotNull
    private List<String> withPermissions;

    @JsonProperty("without_permissions")
    @NotNull
    private List<String> withoutPermissions;



    public static RoleInfoDTO fromHomeRoleEntity(HomeRoleEntity homeRoleEntity) {
        return RoleInfoDTO.builder()
                .roleName(homeRoleEntity.getName())
                .roleIconImageReference(homeRoleEntity.getBase64EncodedImage())
                .build();
    }

    public static HomeRoleEntity toHomeRoleEntity(RoleInfoDTO roleInfoDTO) {
        return HomeRoleEntity.builder()
                .name(roleInfoDTO.getRoleName())
                .base64EncodedImage(roleInfoDTO.roleIconImageReference)
                .build();
    }

}

