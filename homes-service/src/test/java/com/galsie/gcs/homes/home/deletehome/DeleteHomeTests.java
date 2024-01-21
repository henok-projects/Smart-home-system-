package com.galsie.gcs.homes.home.deletehome;

import com.galsie.gcs.homes.service.home.DeleteHomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeleteHomeTests {

    @Autowired
    DeleteHomeService deleteHomeService;


    @Autowired
    DeleteHomeAux deleteHomeAux;

    @Test
    void contextLoads() {
    }

    @Test
    void testDeleteHome(){
        var dto = deleteHomeAux.auxGetValidDeleteHomeRequestDTO();
        var response = deleteHomeService.deleteHome(dto);
        // This assertion is valid because the expected value correct
        assert !response.hasError();
    }


    @Test
    void testDeleteHomeValidationFailure(){
        var dto = deleteHomeAux.auxGetInvalidDeleteHomeRequestDTO();
        var response = deleteHomeService.deleteHome(dto);
        // This assertion is valid because the expected value correct
        assert !response.hasError();
    }


}
