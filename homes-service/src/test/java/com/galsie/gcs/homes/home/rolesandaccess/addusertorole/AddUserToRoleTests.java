package com.galsie.gcs.homes.home.rolesandaccess.addusertorole;


import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AddUserToRoleTests {

    @Autowired
    HomeRolesAndAccessService homeRolesAndAccessService;

    AddUserToRoleAux addUserToRoleAux = new AddUserToRoleAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidAddUserToRoleTests(){
        var dto = addUserToRoleAux.auxGetValidAddUserListToRoleRequestDTO();
        var response = homeRolesAndAccessService.addUserToRole(dto);
        assert !response.hasError();
    }

    @Test
    void testInvalidAddUserToRoleTests(){
        var dto = addUserToRoleAux.auxGetInvalidAddUserListToRoleRequestDTO();
        var response = homeRolesAndAccessService.addUserToRole(dto);
        assert !response.hasError();
    }
}
