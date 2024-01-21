package com.galsie.gcs.homes.home.rolesandaccess.editroleidentity;


import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EditRoleIdentityTests {

    @Autowired
    HomeRolesAndAccessService homeRolesAndAccessService;

    EditRoleIdentityAux editRoleIdentityAux = new EditRoleIdentityAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidEditRoleIdentity(){
        var dto = editRoleIdentityAux.auxGetValidEditHomeRoleIdentityRequestDTO();
        var response = homeRolesAndAccessService.editRoleIdentity(dto);
        assert !response.hasError();
    }

    @Test
    void testInvalidEditRoleIdentity(){
        var dto = editRoleIdentityAux.auxGetInvalidEditHomeRoleIdentityRequestDTO();
        var response = homeRolesAndAccessService.editRoleIdentity(dto);
        assert !response.hasError();
    }
}
