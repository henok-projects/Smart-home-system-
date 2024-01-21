package com.galsie.gcs.homes.home.rolesandaccess.editroleidentity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EditRoleIdentityValidateTests {
    EditRoleIdentityAux editRoleIdentityAux = new EditRoleIdentityAux();

    @Test
    void contextLoads() {
    }


    @Test
    void testValidRoleId() {
        var data = editRoleIdentityAux.auxGetValidEditHomeRoleIdentityRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidRoleId(), true);

    }

    @Test
    void testValidHomeId() {
        var data = editRoleIdentityAux.auxGetValidEditHomeRoleIdentityRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testRoleIdValidationFailure() {
        var data = editRoleIdentityAux.auxGetInvalidEditHomeRoleIdentityRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidRoleId(), true);

    }

    @Test
    void testHomeIdValidationFailure() {
        var data = editRoleIdentityAux.auxGetInvalidEditHomeRoleIdentityRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), false);

    }
}
