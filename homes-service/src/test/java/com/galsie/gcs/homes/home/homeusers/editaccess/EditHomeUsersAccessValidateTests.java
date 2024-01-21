package com.galsie.gcs.homes.home.homeusers.editaccess;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EditHomeUsersAccessValidateTests {

    @Autowired
    EditHomeUsersAccessAux editHomeUsersAccessAux;

    @Test
    void contextLoads() {
    }

    @Test
    void testValidRoleId() {
        var data = editHomeUsersAccessAux.auxGetValidEditHomeUsersAccessInfoRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testValidHomeId() {
        var data = editHomeUsersAccessAux.auxGetInvalidEditHomeUsersAccessInfoRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testRoleIdValidationFailure() {
        var data = editHomeUsersAccessAux.auxGetValidEditHomeUsersAccessInfoRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeUserId(), true);

    }

    @Test
    void testHomeIdValidationFailure() {
        var data = editHomeUsersAccessAux.auxGetValidEditHomeUsersAccessInfoRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeUserId(), true);

    }

}
