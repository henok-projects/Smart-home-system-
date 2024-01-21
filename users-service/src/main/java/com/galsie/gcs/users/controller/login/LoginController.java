package com.galsie.gcs.users.controller.login;

import com.galsie.gcs.users.data.dto.login.request.GalsieEmailLoginRequestDTO;
import com.galsie.gcs.users.data.dto.login.request.GalsiePhoneLoginRequestDTO;
import com.galsie.gcs.users.data.dto.login.request.GalsieUsernameLoginRequestDTO;
import com.galsie.gcs.users.service.login.GalsieLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    GalsieLoginService loginService;

    @PostMapping("/galsie/username")
    public ResponseEntity<?> loginGalsie(@RequestBody GalsieUsernameLoginRequestDTO loginCredentialsDTO) {
        return loginService.loginUser(loginCredentialsDTO).toResponseEntity();
    }

    @PostMapping("/galsie/email")
    public ResponseEntity<?> loginGalsie(@RequestBody GalsieEmailLoginRequestDTO loginCredentialsDTO) {
        return loginService.loginUser(loginCredentialsDTO).toResponseEntity();
    }

    @PostMapping("/galsie/phone")
    public ResponseEntity<?> loginGalsie(@RequestBody GalsiePhoneLoginRequestDTO loginCredentialsDTO) {
        return loginService.loginUser(loginCredentialsDTO).toResponseEntity();
    }

}

