package com.galsie.gcs.homes.home.rolesandaccess.editpermission;

import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EditPermissionTests {

    @Autowired
    HomeRolesAndAccessService homeRolesAndAccessService;

    EditPermissionAux editPermissionAux = new EditPermissionAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidEditPermission(){
        var dto = editPermissionAux.auxGetValidEditHomeRolePermissionRequestDTO();
        var response = homeRolesAndAccessService.editRolePermissions(dto);
        assert !response.hasError();
    }

    @Test
    void testInvalidEditPermission(){
        var dto = editPermissionAux.auxGetInvalidEditHomeRolePermissionRequestDTO();
        var response = homeRolesAndAccessService.editRolePermissions(dto);
        assert !response.hasError();
    }
}
