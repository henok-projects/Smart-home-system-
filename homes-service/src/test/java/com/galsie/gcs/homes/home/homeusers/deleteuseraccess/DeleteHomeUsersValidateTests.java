package com.galsie.gcs.homes.home.homeusers.deleteuseraccess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DeleteHomeUsersValidateTests {

    @Autowired
    DeleteHomeUsersAux deleteHomeUsersAux;

    @Test
    void contextLoads() {
    }

    @Test
    void testValidRoleId() {
        var data = deleteHomeUsersAux.auxGetValidDeleteHomeUsersRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testValidHomeId() {
        var data = deleteHomeUsersAux.auxGetInvalidDeleteHomeUsersRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), false);

    }

    @Test
    void testRoleIdValidationFailure() {
        var data = deleteHomeUsersAux.auxGetValidDeleteHomeUsersRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeUserId(), true);

    }

    @Test
    void testHomeIdValidationFailure() {
        var data = deleteHomeUsersAux.auxGetInvalidDeleteHomeUsersRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeUserId(), false);

    }
}
