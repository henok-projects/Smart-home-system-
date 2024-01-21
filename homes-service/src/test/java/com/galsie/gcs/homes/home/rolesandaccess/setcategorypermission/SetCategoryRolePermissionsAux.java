package com.galsie.gcs.homes.home.rolesandaccess.setcategorypermission;

import com.galsie.gcs.homes.data.dto.roleandpermissions.setcategoryhomerolepermissions.request.SetSubCategoryHomeRolePermissionRequestDTO;

import java.util.Collections;
import java.util.Random;

public class SetCategoryRolePermissionsAux {

    Random random = new Random();

    private String auxGetValidRandomName() {
        return (random.nextInt(26) + "abcd");
    }

    private String auxGetInvalidRandomName() {
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


    public SetSubCategoryHomeRolePermissionRequestDTO auxGetValidSetCategoryHomeRolePermissionRequestDTO(){

        return new SetSubCategoryHomeRolePermissionRequestDTO((long)auxGetRandomNumber(), (long)auxGetRandomNumber(), auxGetInvalidRandomName(),auxGetInvalidRandomName(), Collections.singletonList(auxGetValidRandomName()), Collections.singletonList(auxGetValidRandomName()));
    }

    public SetSubCategoryHomeRolePermissionRequestDTO auxGetInvalidSetCategoryHomeRolePermissionRequestDTO(){

        return new SetSubCategoryHomeRolePermissionRequestDTO((long)auxGetRandomNegNumber(), (long)auxGetRandomNegNumber(), auxGetInvalidRandomName(),auxGetInvalidRandomName(), Collections.singletonList(auxGetValidRandomName()), Collections.singletonList(auxGetValidRandomName()));
    }
}
