package com.galsie.gcs.homes.home.rolesandaccess.setcategorypermission;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SetCategoryRolePermissionsValidationTests {
    SetCategoryRolePermissionsAux setCategoryRolePermissionsAux = new SetCategoryRolePermissionsAux();

    @Test
    void contextLoads() {
    }


    @Test
    void testValidRoleId() {
        var data = setCategoryRolePermissionsAux.auxGetValidSetCategoryHomeRolePermissionRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidRoleId(), true);

    }

    @Test
    void testValidHomeId() {
        var data = setCategoryRolePermissionsAux.auxGetValidSetCategoryHomeRolePermissionRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testRoleIdValidationFailure() {
        var data = setCategoryRolePermissionsAux.auxGetInvalidSetCategoryHomeRolePermissionRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidRoleId(), false);

    }

    @Test
    void testHomeIdValidationFailure() {
        var data = setCategoryRolePermissionsAux.auxGetInvalidSetCategoryHomeRolePermissionRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), false);

    }
}
