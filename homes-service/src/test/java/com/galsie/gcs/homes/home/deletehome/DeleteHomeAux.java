package com.galsie.gcs.homes.home.deletehome;

import com.galsie.gcs.homes.data.dto.deletehome.request.DeleteHomeRequestDTO;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DeleteHomeAux {

    Random random = new Random();

    private boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    private long auxGetRandomNumber() {
        return random.nextInt(Integer.MAX_VALUE) * 1;
    }

    private long auxGetRandomNegNumber() {
        return random.nextInt(Integer.MAX_VALUE) * -1;
    }


    public DeleteHomeRequestDTO auxGetValidDeleteHomeRequestDTO(){
        return new DeleteHomeRequestDTO(auxGetRandomNumber(),getRandomBoolean());
    }

    public DeleteHomeRequestDTO auxGetInvalidDeleteHomeRequestDTO(){
        return new DeleteHomeRequestDTO(auxGetRandomNegNumber(),getRandomBoolean());
    }

}
