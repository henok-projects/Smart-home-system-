package com.galsie.gcs.homes.home.rolesandaccess.addrole;

import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AddHomeRoleTests {

    @Autowired
    HomeRolesAndAccessService homeRolesAndAccessService;

    AddHomeRoleAux addHomeRoleAux = new AddHomeRoleAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidAddHomeRole(){
        var dto = addHomeRoleAux.auxGetValidAddHomeRoleRequestDTO();
        var response = homeRolesAndAccessService.addHomeRole(dto);
        assert !response.hasError();
    }

    @Test
    void testInvalidAddHomeRole(){
        var dto = addHomeRoleAux.auxGetValidAddHomeRoleRequestDTO();
        var response = homeRolesAndAccessService.addHomeRole(dto);
        assert !response.hasError();
    }
}
