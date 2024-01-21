package com.galsie.gcs.homes.home;

import com.galsie.gcs.homes.data.dto.common.HomeAddressDTO;
import com.galsie.gcs.homes.data.dto.addhome.request.AddHomeRequestDTO;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class AddHomeAux {

    Random random = new Random();

    private String auxGetValidRandomName(){
        return (random.nextInt(26) + "abcd");
    }

    private String auxGetInvalidRandomName(){
        return (random.nextInt(26) + "abcd");
    }
    private double auxGetRandomNumber(){
        int numIterations = 10;
        int randomNumber = 0;
        for (int i = 0; i < numIterations; i++) {
            randomNumber = random.nextInt(100);
        }
        return  randomNumber;
    }

    private String auxGetRandom() {
        return (random.nextInt(26) + "a");
    }

    private Integer auxGetRandomNegNumber() {
        return random.nextInt(Integer.MAX_VALUE) * -1;
    }

    public AddHomeRequestDTO auxGetValidAddHomeRequestDTO(){

        Map<String,String> homeFloors = new HashMap<>();
        homeFloors.put("1",auxGetValidRandomName());
        homeFloors.put("2",auxGetValidRandomName());
        homeFloors.put("3",auxGetValidRandomName());
        return new AddHomeRequestDTO(auxGetValidRandomName(), auxGetValidRandomName(), new HomeAddressDTO((long)auxGetRandomNumber(), (long)auxGetRandomNumber(), (long)auxGetRandomNumber(), auxGetValidRandomName(), auxGetRandom(), auxGetValidRandomName(), auxGetRandomNumber(), auxGetRandomNumber()), homeFloors);
    }

    public AddHomeRequestDTO auxGetInvalidAddHomeRequestDTO(){

        Map<String,String> homeFloors = new HashMap<>();
        homeFloors.put(auxGetRandomNegNumber().toString(),auxGetInvalidRandomName());
        homeFloors.put(auxGetRandomNegNumber().toString(),auxGetInvalidRandomName());
        homeFloors.put(auxGetRandomNegNumber().toString(),auxGetInvalidRandomName());
        return new AddHomeRequestDTO(auxGetRandom(), auxGetInvalidRandomName(), new HomeAddressDTO((long)auxGetRandomNumber(), (long)auxGetRandomNumber(), (long)auxGetRandomNumber(), auxGetInvalidRandomName(), auxGetInvalidRandomName(), auxGetInvalidRandomName(), auxGetRandomNumber(), auxGetRandomNumber()), homeFloors);
    }
}
