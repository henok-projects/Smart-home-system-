package com.galsie.gcs.homes.home.homeusers.editaccess;

import com.galsie.gcs.homes.service.home.homeusers.HomeUserManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EditHomeUsersAccessTests {

    @Autowired
    HomeUserManagementService homeUserManagementService;
    @Autowired
    EditHomeUsersAccessAux editHomeUsersAccessAux;

    @Test
    void contextLoads() {
    }
    @Test
    void testValidEditUserAccess(){
        var dto = editHomeUsersAccessAux.auxGetValidEditHomeUsersAccessInfoRequestDTO();
        var response = homeUserManagementService.editUserAccess(dto);
        assert response.hasError();
    }

    @Test
    void testInvalidEditUserAccess(){
        var dto = editHomeUsersAccessAux.auxGetInvalidEditHomeUsersAccessInfoRequestDTO();
        var response = homeUserManagementService.editUserAccess(dto);
        assert response.hasError();
    }
}
