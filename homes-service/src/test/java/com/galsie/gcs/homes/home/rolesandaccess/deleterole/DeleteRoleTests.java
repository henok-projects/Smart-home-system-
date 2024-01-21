package com.galsie.gcs.homes.home.rolesandaccess.deleterole;

import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeleteRoleTests {

    @Autowired
    HomeRolesAndAccessService homeRolesAndAccessService;

    DeleteRoleAux deleteRoleAux = new DeleteRoleAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidDeleteHomeRole(){
        var dto = deleteRoleAux.auxGetValidDeleteHomeRoleRequestDTO();
        var response = homeRolesAndAccessService.deleteHomeRole(dto);
        assert !response.hasError();
    }

    @Test
    void testInvalidDeleteHomeRole(){
        var dto = deleteRoleAux.auxGetInvalidDeleteHomeRoleRequestDTO();
        var response = homeRolesAndAccessService.deleteHomeRole(dto);
        assert !response.hasError();
    }
}
