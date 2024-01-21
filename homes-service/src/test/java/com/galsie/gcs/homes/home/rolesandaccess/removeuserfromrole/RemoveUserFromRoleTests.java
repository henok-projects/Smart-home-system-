package com.galsie.gcs.homes.home.rolesandaccess.removeuserfromrole;


import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RemoveUserFromRoleTests {

    @Autowired
    HomeRolesAndAccessService homeRolesAndAccessService;

    RemoveUserFromRoleAux removeUserFromRoleAux = new RemoveUserFromRoleAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidRemoveUserFromRole(){
        var dto = removeUserFromRoleAux.auxGetValidRemoveUserListFromRoleRequestDTO();
        var response = homeRolesAndAccessService.removeUsersFromRole(dto);
        assert !response.hasError();
    }

    @Test
    void testInvalidRemoveUserFromRole(){
        var dto = removeUserFromRoleAux.auxGetInvalidRemoveUserListFromRoleRequestDTO();
        var response = homeRolesAndAccessService.removeUsersFromRole(dto);
        assert !response.hasError();
    }
}
