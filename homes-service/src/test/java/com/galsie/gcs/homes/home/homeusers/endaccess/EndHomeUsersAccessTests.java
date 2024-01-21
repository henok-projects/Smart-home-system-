package com.galsie.gcs.homes.home.homeusers.endaccess;

import com.galsie.gcs.homes.service.home.homeusers.HomeUserManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EndHomeUsersAccessTests {

    @Autowired
    HomeUserManagementService homeUserManagementService;

    @Autowired
    EndHomeUsersAccessAux endHomeUsersAccessAux;

    @Test
    void contextLoads() {
    }

    @Test
    void testValidEndUserAccess(){
        var dto = endHomeUsersAccessAux.auxGetValidEndHomeUsersAccessRequestDTO();
        var response = homeUserManagementService.endUserAccess(dto);
        assert response.hasError();
    }

    @Test
    void testInvalidEndUserAccess(){
        var dto = endHomeUsersAccessAux.auxGetInvalidEndHomeUsersAccessRequestDTO();
        var response = homeUserManagementService.endUserAccess(dto);
        assert response.hasError();
    }
}
