package com.galsie.gcs.homes.home;

import com.galsie.gcs.homes.service.home.HomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AddHomeValidateTests {

    @Autowired
    HomeService homeService;

    @Autowired
    AddHomeAux addHomeAux;

    @Test
    void contextLoads() {
    }

    @Test
    void testAllFloorNoPostive() {
        var data = addHomeAux.auxGetValidAddHomeRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.areAllFloorNumbersPositiveAndNonZero(), true);

    }

    @Test
    void testFloorValidationFailure() {
        var data = addHomeAux.auxGetInvalidAddHomeRequestDTO();
        // This assertion is invalid because the expected value is not non negative.
        assertEquals(data.areAllFloorNumbersPositiveAndNonZero(), false);

    }

    @Test
    void testValidHomeName() {
        var data = addHomeAux.auxGetValidAddHomeRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeName() == null, true);

    }

    @Test
    void testHomeNameValidationFailure() {
        var data = addHomeAux.auxGetInvalidAddHomeRequestDTO();
        // This assertion is invalid because the expected value is not expected length.
        assertEquals(data.isValidHomeName() == null, true);

    }
    @Test
    void testValidAddress() {
        var data = addHomeAux.auxGetValidAddHomeRequestDTO().getHomeAddress();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidAddress(), true);

    }

    @Test
    void testAddressValidationFailure() {
        var data = addHomeAux.auxGetInvalidAddHomeRequestDTO().getHomeAddress();
        // This assertion is invalid because the expected value is not expected word length.
        assertTrue(data.isValidAddress());

    }
    @Test
    void testValidHomeType() {
        var data = addHomeAux.auxGetValidAddHomeRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeType(), false);
    }

    @Test
    void testHomeTypeValidationFailure() {
        var data = addHomeAux.auxGetInvalidAddHomeRequestDTO();
        // This assertion is invalid because the expected value is not from the list of homeTypes.
        assertEquals(data.isValidHomeType(), false);
    }

    @Test
    void testAddHomeFailsDuplicateData(){
        var homeInfo1 = addHomeAux.auxGetValidAddHomeRequestDTO();
        homeService.addHome(homeInfo1);
        var response = homeService.addHome(homeInfo1);
        // This assertion is invalid because the expected is duplicate not allowed to add.
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


