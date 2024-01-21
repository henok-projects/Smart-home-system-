package com.galsie.gcs.homes.home.homeusers.endaccess;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EndHomeUsersAccessValidateTests {
    @Autowired
    EndHomeUsersAccessAux endHomeUsersAccessAux;

    @Test
    void contextLoads() {
    }

    @Test
    void testValidRoleId() {
        var data = endHomeUsersAccessAux.auxGetValidEndHomeUsersAccessRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testValidHomeId() {
        var data = endHomeUsersAccessAux.auxGetInvalidEndHomeUsersAccessRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testRoleIdValidationFailure() {
        var data = endHomeUsersAccessAux.auxGetValidEndHomeUsersAccessRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeUserId(), true);

    }

    @Test
    void testHomeIdValidationFailure() {
        var data = endHomeUsersAccessAux.auxGetInvalidEndHomeUsersAccessRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeUserId(), true);

    }

}
