package com.galsie.gcs.homes.home.homeusers.deleteuseraccess;

import com.galsie.gcs.homes.data.dto.homeuseraccess.deleteuser.request.DeleteHomeUsersRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Random;

@Component
public class DeleteHomeUsersAux {
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


    public DeleteHomeUsersRequestDTO auxGetValidDeleteHomeUsersRequestDTO(){

        return new DeleteHomeUsersRequestDTO((long)auxGetRandomNumber(), Collections.singletonList((long) auxGetRandomNumber()));
    }

    public DeleteHomeUsersRequestDTO auxGetInvalidDeleteHomeUsersRequestDTO(){

        return new DeleteHomeUsersRequestDTO(auxGetRandomNegNumber(), Collections.singletonList(auxGetRandomNegNumber()));
    }

}
