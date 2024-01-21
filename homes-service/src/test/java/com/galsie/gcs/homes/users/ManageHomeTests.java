package com.galsie.gcs.homes.users;

import com.galsie.gcs.homes.service.home.HomeService;
import com.galsie.gcs.homes.service.users.UserHomeManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ManageHomeTests {

    @Autowired
    HomeService homeService;

    @Autowired
    UserHomeManagementService userHomeManagementService;

    ManageHomeAux manageHomeAux = new ManageHomeAux();

    @Test
    void contextLoads() {
    }

    @Test
    void testHomeArchive(){
        var dto = manageHomeAux.auxGetHomeArchiveRequest();
        var response = userHomeManagementService.archiveHome(dto);
        // This assertion is valid because the expected value correct
        assert response.hasError();
    }


    @Test
    void testHomeLeave(){
        var dto = manageHomeAux.auxGetHomeLeaveRequest();
        var response = userHomeManagementService.leaveHome(dto);
        // This assertion is valid because the expected value correct
        assert response.hasError();
    }
    @Test
    void testGetHomeInfo(){
        var dto = manageHomeAux.auxGetHomeInfoRequest();
        var response = homeService.getBasicHomeInfo(dto);
        // This assertion is valid because the expected value correct
        assert response.hasError();
    }

}
