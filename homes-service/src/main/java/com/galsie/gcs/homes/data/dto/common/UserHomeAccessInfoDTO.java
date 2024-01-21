package com.galsie.gcs.homes.data.dto.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserAccessInfoEntity;
import com.galsie.gcs.homes.infrastructure.rolesandaccess.permissions.format.HomePermissionsFormatter;
import com.galsie.gcs.homes.data.entity.home.invites.HomeInviteAccessInfoEntity;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserHomeAccessInfoDTO {

    @JsonProperty("access_start_date")
    @Nullable
    private String accessStartDate;

    @JsonProperty("access_end_date")
    @Nullable
    private String accessEndDate;

    @JsonProperty("roles")
    @NotNull
    private List<Long> roles;

    @NotNull
    private List<String> permissions;


    public static HomeInviteAccessInfoEntity toInviteAccessInfoEntity(UserHomeAccessInfoDTO userHomeAccessInfoDTO) {
        // Regex Formatter instance
        var regexFormatter = HomePermissionsFormatter.getRegexFormatterInstance();

        // Filter and format permissions
        var formattedWithPermissions = userHomeAccessInfoDTO.permissions.stream()
                .map(regexFormatter::format)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());


        List<HomeRoleEntity> roles = userHomeAccessInfoDTO.roles.stream()
                .map(id -> {
                    HomeRoleEntity entity = new HomeRoleEntity();
                    entity.setId(id);
                    return entity;
                })
                .collect(Collectors.toList());


        return HomeInviteAccessInfoEntity.builder()
                .accessStartDate(userHomeAccessInfoDTO.accessStartDate != null && !userHomeAccessInfoDTO.accessStartDate.isEmpty()
                        ? LocalDateTime.parse(userHomeAccessInfoDTO.accessStartDate)
                        : null)
                .accessEndDate(userHomeAccessInfoDTO.accessEndDate != null && !userHomeAccessInfoDTO.accessEndDate.isEmpty()
                        ? LocalDateTime.parse(userHomeAccessInfoDTO.accessEndDate)
                        : null)
                .roles(roles)
                .withPermissions(formattedWithPermissions)
                .build();
    }

    public static UserHomeAccessInfoDTO fromInviteAccessInfoEntity(HomeInviteAccessInfoEntity inviteAccessInfo) {
        if (inviteAccessInfo == null) {
            return null;
        }

        List<Long> roleIds = inviteAccessInfo.getRoles() != null
                ? inviteAccessInfo.getRoles().stream().map(HomeRoleEntity::getId).collect(Collectors.toList())
                : new ArrayList<>();

        String accessStartDate = inviteAccessInfo.getAccessStartDate() != null
                ? inviteAccessInfo.getAccessStartDate().toString()
                : null;

        String accessEndDate = inviteAccessInfo.getAccessEndDate() != null
                ? inviteAccessInfo.getAccessEndDate().toString()
                : null;

        return UserHomeAccessInfoDTO.builder()
                .accessStartDate(accessStartDate)
                .accessEndDate(accessEndDate)
                .roles(roleIds)
                .permissions(inviteAccessInfo.getWithPermissions())
                .build();
    }

    public static UserHomeAccessInfoDTO fromUserAccessInfoEntity(HomeUserAccessInfoEntity inviteAccessInfo) {
        if (inviteAccessInfo == null) {
            return null;
        }

        List<Long> roleIds = inviteAccessInfo.getRoles() != null
                ? inviteAccessInfo.getRoles().stream().map(HomeRoleEntity::getId).collect(Collectors.toList())
                : new ArrayList<>();

        String accessStartDate = inviteAccessInfo.getAccessStartDate() != null
                ? inviteAccessInfo.getAccessStartDate().toString()
                : null;

        String accessEndDate = inviteAccessInfo.getAccessEndDate() != null
                ? inviteAccessInfo.getAccessEndDate().toString()
                : null;

        return UserHomeAccessInfoDTO.builder()
                .accessStartDate(accessStartDate)
                .accessEndDate(accessEndDate)
                .roles(roleIds)
                .permissions(inviteAccessInfo.getWithPermissions())
                .build();
    }
}

