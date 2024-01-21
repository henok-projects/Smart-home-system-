package com.galsie.gcs.users.controller.checks;

import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticationStrategy;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.users.data.dto.registration.request.checks.CheckExistsUserWithEmailRequestDTO;
import com.galsie.gcs.users.data.dto.registration.request.checks.CheckExistsUserWithPhoneNumberRequestDTO;
import com.galsie.gcs.users.data.dto.registration.request.checks.CheckExistsUserWithUsernameRequestDTO;
import com.galsie.gcs.users.service.register.UserExistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checks")
public class CheckUserExistenceController {
    @Autowired
    UserExistenceService userExistenceService;

    @AuthenticatedGCSRequest(authenticationStrategy = AuthenticationStrategy.OR, authSessionTypes = {GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT})
    @PostMapping("existsUsername")
    ResponseEntity<?> existUsername(@RequestBody CheckExistsUserWithUsernameRequestDTO checkExistsUserWithUsernameRequestDTO) {
        return userExistenceService.existsUserWithUsername(checkExistsUserWithUsernameRequestDTO).toResponseEntity();
    }

    @AuthenticatedGCSRequest(authenticationStrategy = AuthenticationStrategy.OR, authSessionTypes = {GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT})
    @PostMapping("existsEmail")
    ResponseEntity<?> existEmail(@RequestBody CheckExistsUserWithEmailRequestDTO checkExistsUserWithEmailRequestDTO) {
        return userExistenceService.existsUserWithEmail(checkExistsUserWithEmailRequestDTO).toResponseEntity();
    }

    @AuthenticatedGCSRequest(authenticationStrategy = AuthenticationStrategy.OR, authSessionTypes = {GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT})
    @PostMapping("existsPhone")
    ResponseEntity<?> existsPhone(@RequestBody CheckExistsUserWithPhoneNumberRequestDTO checkExistsUserWithPhoneNumberRequestDTO) {
        return userExistenceService.existsUserWithPhone(checkExistsUserWithPhoneNumberRequestDTO).toResponseEntity();
    }
}
