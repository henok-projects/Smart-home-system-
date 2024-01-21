package com.galsie.gcs.users.controller.registration;

import com.galsie.gcs.users.data.dto.registration.request.register.RegisterNewGalsieAccountRequestDTO;
import com.galsie.gcs.users.service.register.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    UserRegistrationService userRegistrationService;

    @PostMapping("/register/galsie")
    public ResponseEntity<?> registerGalsie(@RequestBody RegisterNewGalsieAccountRequestDTO registerNewGalUserAccountDTO){
        return userRegistrationService.registerNewGalUserAccount(registerNewGalUserAccountDTO).toResponseEntity();
    }

}
