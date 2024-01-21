package com.galsie.gcs.homes.home.area;

import com.galsie.gcs.homes.data.discrete.HomeAreaType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AddHomeAreaValidateTests {

    AddHomeAreaAux addHomeAreaAux = new AddHomeAreaAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testValidAreaDTO(){
        var data = addHomeAreaAux.auxGetRequestDto();
        data.getAreaDetails().setAreaType(HomeAreaType.BEDROOM.name());
        assertEquals(data.validate().isEmpty(), false);

    }
}
