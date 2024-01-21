package com.galsie.gcs.users.controller.usersecuritypin;


import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.users.data.dto.editprofileinfo.pin.request.SetupUserSecurityAppPinRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.twofactorauth.request.SetupUserSecurity2FARequestDTO;
import com.galsie.gcs.users.service.usersecuritypin.EditUserAccountSecurityPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security/pin")
public class UserAccountSecurityPreferenceController {

    @Autowired
    private EditUserAccountSecurityPreferenceService securityPreferenceService;

    @PostMapping("/setup")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> setupPin(@RequestBody SetupUserSecurityAppPinRequestDTO SetupUserSecurityAppPinRequestDTO) {
        return securityPreferenceService.requestSetUpPin(SetupUserSecurityAppPinRequestDTO).toResponseEntity();
    }

    @GetMapping("/getPin")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> getPin(){
        return securityPreferenceService.gcsInternalRequestGetPin().toResponseEntity();
    }

    @PostMapping("/2fa/setup")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> setUp2FA(@RequestBody SetupUserSecurity2FARequestDTO request) {
        return securityPreferenceService.requestSetUp2FA(request).toResponseEntity();
    }

}


