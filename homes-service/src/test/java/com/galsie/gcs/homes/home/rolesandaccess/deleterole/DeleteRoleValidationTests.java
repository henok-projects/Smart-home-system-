package com.galsie.gcs.homes.home.rolesandaccess.deleterole;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DeleteRoleValidationTests {

    DeleteRoleAux deleteRoleAux = new DeleteRoleAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidRoleId() {
        var data = deleteRoleAux.auxGetValidDeleteHomeRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidRoleId(), true);

    }

    @Test
    void testValidHomeId() {
        var data = deleteRoleAux.auxGetValidDeleteHomeRoleRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), false);

    }

    @Test
    void testRoleIdValidationFailure() {
        var data = deleteRoleAux.auxGetValidDeleteHomeRoleRequestDTO();
        // This assertion is invalid because the expected value is not non negative.
        assertEquals(data.isValidRoleId(), true);

    }

    @Test
    void testHomeIdValidationFailure() {
        var data = deleteRoleAux.auxGetValidDeleteHomeRoleRequestDTO();
        // This assertion is invalid because the expected value is not non negative.
        assertEquals(data.isValidHomeId(), false);

    }
}
