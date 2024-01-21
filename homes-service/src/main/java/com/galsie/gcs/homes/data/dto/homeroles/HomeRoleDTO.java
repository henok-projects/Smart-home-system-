package com.galsie.gcs.homes.data.dto.homeroles;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//this is a dto for sharing the HomeRoleEntity
public class HomeRoleDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @JsonProperty("name")
    @NotNull
    private String name;

    @JsonProperty("image")
    @Nullable
    private String base64EncodedImage;


    @JsonProperty("with_permissions")
    @Nullable
    List<String> withPermissions;

    @JsonProperty("without_permissions")
    @Nullable
    List<String> withoutPermissions;


    public static List<HomeRoleEntity> toHomeRoleEntityList(List<HomeRoleDTO> homeRoleDTOList) {

        return homeRoleDTOList.stream()
                .map(homeRoleDTO -> HomeRoleEntity.builder()
                        .id(homeRoleDTO.getId())
                        .build())
                .collect(Collectors.toList());
    }


    public static List<HomeRoleDTO> fromHomeRoleEntity(HomeRoleEntity homeRoleEntity) {
        return homeRoleEntity.getHome().getHomeRoleEntities().stream()
                .map(homeRole -> HomeRoleDTO.builder()
                        .id(homeRole.getId())
                        .build()).collect(Collectors.toList());
    }


}

