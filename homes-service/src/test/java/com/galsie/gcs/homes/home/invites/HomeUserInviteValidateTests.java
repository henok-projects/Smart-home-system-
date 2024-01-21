package com.galsie.gcs.homes.home.invites;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HomeUserInviteValidateTests {
    HomeUserInviteAux aux = new HomeUserInviteAux();
    @Test
    void contextLoads() {
    }

    @Test
    void testValidUsername() {
        var data = aux.auxGetValidInviteUserByUsernameRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);
    }


    @Test
    void testValidHomeId() {
        var data = aux.auxGetValidInviteUserByEmailOrPhoneRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);
    }

}
