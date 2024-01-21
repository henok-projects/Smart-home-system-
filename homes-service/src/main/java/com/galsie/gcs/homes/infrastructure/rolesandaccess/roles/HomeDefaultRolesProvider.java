package com.galsie.gcs.homes.infrastructure.rolesandaccess.roles;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//	its a component because it may scale to autowire some items
@Component
public class HomeDefaultRolesProvider {

    private static final HomeDefaultRole HOUSE_MASTER_ROLE = createHouseMasterRole();
    private static final HomeDefaultRole ADMIN_ROLE = createAdminRole();
    private static final HomeDefaultRole OCCUPANT_ROLE = createOccupantRole();
    private static final HomeDefaultRole VISITOR_ROLE = createVisitorRole();

    public List<HomeDefaultRole> getHomeDefaultRoles() {
        return Arrays.asList(HOUSE_MASTER_ROLE, ADMIN_ROLE, OCCUPANT_ROLE, VISITOR_ROLE);
    }

    public int getDefaultRoleIndexForAddingHomeUser() {
        return getHomeDefaultRoles().indexOf(HOUSE_MASTER_ROLE);
    }
    private static HomeDefaultRole createHouseMasterRole(){
        List<String> withPermissions = Arrays.asList("*");
        List<String> withoutPermissions = new ArrayList<>();
        return new HomeDefaultRole("HouseMaster", withPermissions, withoutPermissions);
    }


    private static HomeDefaultRole createAdminRole(){
        List<String> withPermissions = Arrays.asList("*");
        List<String> withoutPermissions = Arrays.asList("home.management.deletehome");
        return new HomeDefaultRole("Admin", withPermissions, withoutPermissions);
    }


    private static HomeDefaultRole createOccupantRole(){
        List<String> withPermissions = Arrays.asList(); // add when provided.
        List<String> withoutPermissions = Arrays.asList();
        return new HomeDefaultRole("Occupant", withPermissions, withoutPermissions);
    }


    private static HomeDefaultRole createVisitorRole(){
        List<String> withPermissions = Arrays.asList(); // add when provided
        List<String> withoutPermissions = Arrays.asList();
        return new HomeDefaultRole("Visitor", withPermissions, withoutPermissions);
    }

}
