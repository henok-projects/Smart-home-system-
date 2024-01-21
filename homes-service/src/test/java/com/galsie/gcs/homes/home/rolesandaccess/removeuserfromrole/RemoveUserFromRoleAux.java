package com.galsie.gcs.homes.home.rolesandaccess.removeuserfromrole;

import com.galsie.gcs.homes.data.dto.roleandpermissions.removeuserfromrole.request.RemoveUserListFromRoleRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Random;

@Component
public class RemoveUserFromRoleAux {

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


    public RemoveUserListFromRoleRequestDTO auxGetValidRemoveUserListFromRoleRequestDTO(){

        return new RemoveUserListFromRoleRequestDTO((long)auxGetRandomNumber(),(long)auxGetRandomNumber(), Collections.singletonList((long) auxGetRandomNumber()));
    }

    public RemoveUserListFromRoleRequestDTO auxGetInvalidRemoveUserListFromRoleRequestDTO(){

        return new RemoveUserListFromRoleRequestDTO((long)auxGetRandomNegNumber(),(long)auxGetRandomNegNumber(), Collections.singletonList((long) auxGetRandomNegNumber()));
    }
}
