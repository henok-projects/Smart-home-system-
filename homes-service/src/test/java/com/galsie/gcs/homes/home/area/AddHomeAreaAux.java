package com.galsie.gcs.homes.home.area;

import com.galsie.gcs.homes.data.dto.addarea.request.AddHomeAreaRequestDTO;
import com.galsie.gcs.homes.data.dto.common.HomeAreaDetailsDTO;
import org.junit.jupiter.api.Test;

import java.util.Random;


public class AddHomeAreaAux {

    Random random = new Random();

    @Test
    void contextLoads() {
    }
    private Long auxGetRandomNumber(){
        int numIterations = 10;
        int randomNumber = 0;
        for (int i = 0; i < numIterations; i++) {
            randomNumber = random.nextInt(100);
        }
        return Long.valueOf(randomNumber);
    }

    private String auxGetRandom() {
        return (random.nextInt(26) + "ac");
    }

    private String auxGetValidRandomName(){
        return (random.nextInt(26) + "abcd");
    }

    public AddHomeAreaRequestDTO auxGetRequestDto(){
        return new AddHomeAreaRequestDTO(auxGetRandomNumber(),new HomeAreaDetailsDTO(auxGetValidRandomName(),auxGetValidRandomName(), auxGetValidRandomName(),auxGetValidRandomName()),auxGetRandomNumber());
    }

}
