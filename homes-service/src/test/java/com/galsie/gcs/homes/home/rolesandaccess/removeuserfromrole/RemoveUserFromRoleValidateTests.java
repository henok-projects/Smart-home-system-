package com.galsie.gcs.homes.home.rolesandaccess.removeuserfromrole;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RemoveUserFromRoleValidateTests {

    @Autowired
    RemoveUserFromRoleAux removeUserFromRoleAux;

    @Test
    void contextLoads() {
    }


    @Test
    void testValidRoleId() {
        var data = removeUserFromRoleAux.auxGetValidRemoveUserListFromRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidRoleId(), true);

    }

    @Test
    void testValidHomeId() {
        var data = removeUserFromRoleAux.auxGetValidRemoveUserListFromRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testRoleIdValidationFailure() {
        var data = removeUserFromRoleAux.auxGetInvalidRemoveUserListFromRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidRoleId(), false);

    }

    @Test
    void testHomeIdValidationFailure() {
        var data = removeUserFromRoleAux.auxGetInvalidRemoveUserListFromRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), false);

    }
}
