package com.galsie.gcs.homes.home.deletehome;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DeleteHomeValidationTests {

    @Autowired
    DeleteHomeAux deleteHomeAux;

    @Test
    void contextLoads() {
    }

    @Test
    void testValidHomeId() {
        var data = deleteHomeAux.auxGetValidDeleteHomeRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), true);

    }

    @Test
    void testHomeIdValidationFailure() {
        var data = deleteHomeAux.auxGetInvalidDeleteHomeRequestDTO();
        // This assertion is valid because the expected value correct
        assertEquals(data.isValidHomeId(), false);

    }
}
