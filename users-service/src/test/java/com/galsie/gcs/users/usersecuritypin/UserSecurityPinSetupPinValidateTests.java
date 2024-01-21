package com.galsie.gcs.users.usersecuritypin;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

public class UserSecurityPinSetupPinValidateTests {

    UserSecurityPinSetupPinAux userSecurityPinSetupPinAux = new UserSecurityPinSetupPinAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidHashedPin(){
        var data = userSecurityPinSetupPinAux.auxGetSetupPinRequest();
        data.getHashedPin();
        assertFalse(data.isHashedPwdValid());
    }

    @Test
    void testInvalidHashedPin(){
        var data = userSecurityPinSetupPinAux.auxGetInvalidSetupPinRequest();
        data.getHashedPin();
        assertFalse(data.isHashedPwdValid());
    }


}


