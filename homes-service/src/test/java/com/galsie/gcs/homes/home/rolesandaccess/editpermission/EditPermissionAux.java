package com.galsie.gcs.homes.home.rolesandaccess.editpermission;

import com.galsie.gcs.homes.data.dto.roleandpermissions.editrolepermissions.request.EditHomeRolePermissionRequestDTO;

import java.util.Collections;
import java.util.Random;

public class EditPermissionAux {
    Random random = new Random();

    private String auxGetValidRandomName() {
        return (random.nextInt(26) + "abcd");
    }
    private Long auxGetRandomNegNumber() {
        return random.nextLong(Long.MAX_VALUE) * -1;
    }


    private double auxGetRandomNumber() {
        int numIterations = 10;
        int randomNumber = 0;
        for (int i = 0; i < numIterations; i++) {
            randomNumber = random.nextInt(100);
        }
        return randomNumber;
    }


    public EditHomeRolePermissionRequestDTO auxGetValidEditHomeRolePermissionRequestDTO(){

        return new EditHomeRolePermissionRequestDTO((long)auxGetRandomNumber(), (long)auxGetRandomNumber(), Collections.singletonList(auxGetValidRandomName()), Collections.singletonList(auxGetValidRandomName()));
    }

    public EditHomeRolePermissionRequestDTO auxGetInvalidEditHomeRolePermissionRequestDTO(){

        return new EditHomeRolePermissionRequestDTO(auxGetRandomNegNumber(), (long)auxGetRandomNumber(), Collections.singletonList(auxGetValidRandomName()), Collections.singletonList(auxGetValidRandomName()));
    }
}
