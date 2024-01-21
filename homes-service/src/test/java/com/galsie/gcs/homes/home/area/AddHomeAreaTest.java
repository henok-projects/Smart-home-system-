
package com.galsie.gcs.homes.home.area;

import com.galsie.gcs.homes.service.home.area.HomeAreaManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class AddHomeAreaTest {

    @Autowired
    HomeAreaManagementService homeAreaManagementService;

    AddHomeAreaAux addHomeAreaAux = new AddHomeAreaAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testAddHomeArea(){
        var dto = addHomeAreaAux.auxGetRequestDto();
        var response = homeAreaManagementService.addHomeArea(dto);
        assert response.hasError();
    }

    @Test
    void testAddingAreaWithSameDataWorks(){
        var areaInfo = addHomeAreaAux.auxGetRequestDto();
        var areaInfo1 = addHomeAreaAux.auxGetRequestDto();

        homeAreaManagementService.addHomeArea(areaInfo);
        var response = homeAreaManagementService.addHomeArea(areaInfo1);
        assert response.hasError();
    }


}