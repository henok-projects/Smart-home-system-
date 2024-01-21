package com.galsie.gcs.homes.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ManageHomeValidateTests {

    ManageHomeAux manageHomeAux = new ManageHomeAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidGetHomeInfo(){
        var dto = manageHomeAux.auxGetHomeInfoRequest();
        // This assertion is Invalid because the expected value incorrect
        assertEquals(dto.isValidHomeId(), true);    }
    @Test
    void testfailureGetHomeInfo(){
        var dto = manageHomeAux.auxGetInvalidHomeInfoRequest();
        // This assertion is Invalid because the expected value incorrect
        assertEquals(dto.isValidHomeId(), false);    }


}
