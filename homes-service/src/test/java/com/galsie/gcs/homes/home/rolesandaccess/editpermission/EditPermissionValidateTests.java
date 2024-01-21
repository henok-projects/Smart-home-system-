package com.galsie.gcs.homes.home.rolesandaccess.editpermission;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EditPermissionValidateTests {

    EditPermissionAux editPermissionAux = new EditPermissionAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidRoleId() {
        var data = editPermissionAux.auxGetValidEditHomeRolePermissionRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }


    @Test
    void testHomeIdValidationFailure() {
        var data = editPermissionAux.auxGetInvalidEditHomeRolePermissionRequestDTO();
        // This assertion is invalid because the expected value is not non negative.
        assertEquals(data.isValidHomeId(), false);

    }
}
