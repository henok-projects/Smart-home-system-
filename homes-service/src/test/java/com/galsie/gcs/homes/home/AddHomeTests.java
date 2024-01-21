package com.galsie.gcs.homes.home;


import com.galsie.gcs.homes.service.home.HomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class AddHomeTests {

    @Autowired
    HomeService homeService;

    @Autowired
    AddHomeAux addHomeAux;

    @Test
    void contextLoads() {
    }
    
    @Test
    void testAddHome(){
        var dto = addHomeAux.auxGetValidAddHomeRequestDTO();
        var response = homeService.addHome(dto);
        // This assertion is valid because the expected value correct
        assert !response.hasError();
    }

    @Test
    void testAddHomeValidationFailure(){
        var dto = addHomeAux.auxGetInvalidAddHomeRequestDTO();
        var response = homeService.addHome(dto);
        // This assertion is invalid because the expected value is incorrect.
        assert !response.hasError();

    }

    @Test
    void testAddingHomesWithSameDataWorks(){
        var homeInfo1 = addHomeAux.auxGetValidAddHomeRequestDTO();
        var homeInfo2 = addHomeAux.auxGetValidAddHomeRequestDTO();
        homeService.addHome(homeInfo1);
        var response = homeService.addHome(homeInfo2);
        // This assertion is invalid because the expected is duplicate data not allowed to add.
       assert !response.hasError();
    }


}
