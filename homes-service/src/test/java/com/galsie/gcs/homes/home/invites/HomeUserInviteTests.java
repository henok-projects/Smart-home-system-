package com.galsie.gcs.homes.home.invites;

import com.galsie.gcs.homes.service.home.invites.HomeUserInviteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HomeUserInviteTests {

    @Autowired
    HomeUserInviteService homeUserInviteService;

    @Autowired
    HomeUserInviteAux homeUserInviteAux ;

    @Test
    void contextLoads() {
    }

    @Test
    void testInviteUserByEmailOrPhone(){
        var dto = homeUserInviteAux.auxGetValidInviteUserByEmailOrPhoneRequestDTO();
        var response = homeUserInviteService.requestPerformHomeUserInvite(dto);
        // This assertion is valid because the expected value correct
        assert !response.hasError();
    }


    @Test
    void testInviteUserByUsername(){
        var dto = homeUserInviteAux.auxGetValidInviteUserByUsernameRequestDTO();
        var response = homeUserInviteService.requestPerformHomeUserInvite(dto);
        // This assertion is valid because the expected value correct
        assert !response.hasError();
    }

    @Test
    void testInviteUserByQrCode(){
        var dto = homeUserInviteAux.auxGetInviteUserByQRCodeRequestDTO();
        var response = homeUserInviteService.requestCreateHomeQRInvite(dto);
        // This assertion is valid because the expected value correct
        assert !response.hasError();
    }

}
