package com.galsie.gcs.users.controller.authentication;

import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.user.UserActiveSessionListRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.UserAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.users.service.authentication.LocalUserAuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    @Autowired
    LocalUserAuthenticatorService authenticationService;

    @PostMapping("authenticateUser")
    public ResponseEntity<?> authenticateToken(@RequestBody UserAuthenticationRequestDTO authenticationDTO) {
        return authenticationService.performAuthentication(authenticationDTO).toResponseEntity();
    }

    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.GCS_MICROSERVICE)
    @PostMapping("getUserActiveSessionList")
    public ResponseEntity<?> getActiveSessionList(@RequestBody UserActiveSessionListRequestDTO userActiveSessionListRequestDTO) {
        return authenticationService.getUserAccountActiveSessions(userActiveSessionListRequestDTO).toResponseEntity();
    }

}
