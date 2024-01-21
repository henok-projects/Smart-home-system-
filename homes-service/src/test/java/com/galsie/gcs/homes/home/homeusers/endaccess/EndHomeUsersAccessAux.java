package com.galsie.gcs.homes.home.homeusers.endaccess;

import com.galsie.gcs.homes.data.dto.homeuseraccess.endaccess.request.EndHomeUsersAccessRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Random;

@Component
public class EndHomeUsersAccessAux {

    Random random = new Random();
    private double auxGetRandomNumber() {
        int numIterations = 10;
        int randomNumber = 0;
        for (int i = 0; i < numIterations; i++) {
            randomNumber = random.nextInt(100);
        }
        return randomNumber;
    }


    public EndHomeUsersAccessRequestDTO auxGetValidEndHomeUsersAccessRequestDTO(){
        return new EndHomeUsersAccessRequestDTO((long)auxGetRandomNumber(), Collections.singletonList((long) auxGetRandomNumber()));
    }

    public EndHomeUsersAccessRequestDTO auxGetInvalidEndHomeUsersAccessRequestDTO(){
        return new EndHomeUsersAccessRequestDTO((long)auxGetRandomNumber(), Collections.singletonList((long) auxGetRandomNumber()));
    }

}
