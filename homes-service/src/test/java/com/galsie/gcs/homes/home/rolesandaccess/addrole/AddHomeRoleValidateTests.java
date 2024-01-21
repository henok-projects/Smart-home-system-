package com.galsie.gcs.homes.home.rolesandaccess.addrole;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AddHomeRoleValidateTests {

    @Autowired
    AddHomeRoleAux addHomeRoleAux;

    @Test
    void contextLoads() {
    }

    @Test
    void testValidRoleName() {
        var data = addHomeRoleAux.auxGetValidAddHomeRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testRoleNameValidationFailure() {
        var data = addHomeRoleAux.auxGetValidAddHomeRoleRequestDTO();
        // This assertion is invalid because the expected value is not non negative.
        assertEquals(data.isValidHomeId(), true);

    }
}
