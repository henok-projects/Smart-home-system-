package com.galsie.gcs.users;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
public class EmailTests {
    /*
    @Autowired
    EmailRemoteService emailService;

    @Test
    void contextLoads(){

    }

    @Test
    void emailSends(){
        HashMap<String, String> vars = new HashMap<>();
        vars.put("username", "liwaak");
        var response = emailService.sendEmail(new SendEmailRemoteRequestDTO(SendableEmailType.USER_EMAIL_VERIFICATION, "liwaa@galsie.com", vars));
        assert !response.hasError();
        assert response.getResponseData();
    }
    */
}
