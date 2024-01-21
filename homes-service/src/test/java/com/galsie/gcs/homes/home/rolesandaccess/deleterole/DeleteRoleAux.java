package com.galsie.gcs.homes.home.rolesandaccess.deleterole;

import com.galsie.gcs.homes.data.dto.roleandpermissions.deleterolefromhome.request.DeleteHomeRoleRequestDTO;

import java.util.Random;

public class DeleteRoleAux {
    Random random = new Random();

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


    public DeleteHomeRoleRequestDTO auxGetValidDeleteHomeRoleRequestDTO(){

        return new DeleteHomeRoleRequestDTO((long)auxGetRandomNegNumber(),(long)auxGetRandomNumber());
    }

    public DeleteHomeRoleRequestDTO auxGetInvalidDeleteHomeRoleRequestDTO(){

        return new DeleteHomeRoleRequestDTO(auxGetRandomNegNumber(),auxGetRandomNegNumber());
    }
}
