package com.galsie.gcs.homes.home.rolesandaccess.addrole;

import com.galsie.gcs.homes.data.dto.roleandpermissions.addrole.request.AddHomeRoleRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AddHomeRoleAux {

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


    public AddHomeRoleRequestDTO auxGetValidAddHomeRoleRequestDTO(){

        return new AddHomeRoleRequestDTO((long)auxGetRandomNumber(),auxGetValidRandomName(), auxGetValidRandomName(),(long)auxGetRandomNumber());
    }

    public AddHomeRoleRequestDTO auxGetInvalidAddHomeRoleRequestDTO(){

        return new AddHomeRoleRequestDTO(auxGetRandomNegNumber(),auxGetValidRandomName(), auxGetValidRandomName(),auxGetRandomNegNumber());
    }
}
