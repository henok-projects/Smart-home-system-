package com.galsie.gcs.homes.home.rolesandaccess.setcategorypermission;


import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SetCategoryRolePermissionsTests {

    @Autowired
    HomeRolesAndAccessService homeRolesAndAccessService;

    SetCategoryRolePermissionsAux setCategoryRolePermissionsAux = new SetCategoryRolePermissionsAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidSetCategoryHomeRolePermission(){
        var dto = setCategoryRolePermissionsAux.auxGetValidSetCategoryHomeRolePermissionRequestDTO();
        var response = homeRolesAndAccessService.setCategoryPermissions(dto);
        assert !response.hasError();
    }

    @Test
    void testInvalidSetCategoryHomeRolePermission(){
        var dto = setCategoryRolePermissionsAux.auxGetInvalidSetCategoryHomeRolePermissionRequestDTO();
        var response = homeRolesAndAccessService.setCategoryPermissions(dto);
        assert !response.hasError();
    }
}
