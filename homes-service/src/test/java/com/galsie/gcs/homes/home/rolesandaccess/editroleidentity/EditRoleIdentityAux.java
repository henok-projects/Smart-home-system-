package com.galsie.gcs.homes.home.rolesandaccess.editroleidentity;

import com.galsie.gcs.homes.data.dto.roleandpermissions.editroleidentity.request.EditHomeRoleIdentityRequestDTO;

import java.util.Random;

public class EditRoleIdentityAux {


    Random random = new Random();

    private String auxGetValidRandomName() {
        return (random.nextLong(26) + "abcd");
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


    public EditHomeRoleIdentityRequestDTO auxGetValidEditHomeRoleIdentityRequestDTO() {

        return new EditHomeRoleIdentityRequestDTO((long) auxGetRandomNumber(), (long) auxGetRandomNumber(), auxGetValidRandomName(), auxGetValidRandomName());
    }

    public EditHomeRoleIdentityRequestDTO auxGetInvalidEditHomeRoleIdentityRequestDTO() {

        return new EditHomeRoleIdentityRequestDTO(auxGetRandomNegNumber(), (long) auxGetRandomNumber(), auxGetValidRandomName(), auxGetValidRandomName());
    }
}