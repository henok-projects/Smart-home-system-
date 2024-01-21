package com.galsie.gcs.homes.infrastructure.rolesandaccess.roles;


import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class HomeDefaultRole {

   private String name;
   private List<String> withPermissions; // home.invites.*
   private List<String> withoutPermissions; // home.invites.delete


    public HomeRoleEntity toHomeRoleEntity(HomeEntity home) {
        return HomeRoleEntity.builder()
                .home(home)
                .build();
    }
}
