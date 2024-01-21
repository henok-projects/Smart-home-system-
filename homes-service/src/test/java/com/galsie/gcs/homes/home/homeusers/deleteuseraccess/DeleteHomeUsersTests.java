package com.galsie.gcs.homes.home.homeusers.deleteuseraccess;

import com.galsie.gcs.homes.service.home.homeusers.HomeUserManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeleteHomeUsersTests {

    @Autowired
    HomeUserManagementService homeUserManagementService;

    @Autowired
    DeleteHomeUsersAux deleteHomeUsersAux;

    @Test
    void contextLoads() {
    }

    @Test
    void testValidDeleteHomeUser(){
        var dto = deleteHomeUsersAux.auxGetValidDeleteHomeUsersRequestDTO();
        var response = homeUserManagementService.deleteHomeUsers(dto);
       // assert !response.hasError();
    }

    @Test
    void testInvalidDeleteHomeUser(){
        var dto = deleteHomeUsersAux.auxGetInvalidDeleteHomeUsersRequestDTO();
        var response = homeUserManagementService.deleteHomeUsers(dto);
    }
}
