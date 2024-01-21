package com.galsie.gcs.users.usersecuritypin;


import com.galsie.gcs.users.service.usersecuritypin.EditUserAccountSecurityPreferenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


//@SpringBootTest
public class UserSecurityPinSetupPinTests {

//    @Autowired
    EditUserAccountSecurityPreferenceService editUserAccountSecurityPreferenceService;

    UserSecurityPinSetupPinAux userSecurityPinSetupPinAux = new UserSecurityPinSetupPinAux();

//    @Test
    void contextLoads() {
    }

//    @Test
    void testPinSetup(){
        var dto = userSecurityPinSetupPinAux.auxGetSetupPinRequest();
        var response = editUserAccountSecurityPreferenceService.requestSetUpPin(dto);
        // This assertion is valid because the expected value correct
        assert !response.hasError();
    }


//    @Test
    void testInvalidPinSetup(){
        var dto = userSecurityPinSetupPinAux.auxGetInvalidSetupPinRequest();
        var response = editUserAccountSecurityPreferenceService.requestSetUpPin(dto);
        // This assertion is Invalid because the expected value is incorrect
        assert !response.hasError();
    }

}


