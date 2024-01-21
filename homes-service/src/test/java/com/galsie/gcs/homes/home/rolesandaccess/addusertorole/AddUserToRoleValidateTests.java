package com.galsie.gcs.homes.home.rolesandaccess.addusertorole;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AddUserToRoleValidateTests {

    @Autowired
    AddUserToRoleAux addUserToRoleAux;

    @Test
    void contextLoads() {
    }


    @Test
    void testValidRoleId() {
        var data = addUserToRoleAux.auxGetValidAddUserListToRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidRoleId(), true);

    }

    @Test
    void testValidHomeId() {
        var data = addUserToRoleAux.auxGetValidAddUserListToRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testRoleIdValidationFailure() {
        var data = addUserToRoleAux.auxGetInvalidAddUserListToRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidRoleId(), false);

    }

    @Test
    void testHomeIdValidationFailure() {
        var data = addUserToRoleAux.auxGetInvalidAddUserListToRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), false);

    }
}
