package com.galsie.gcs.homes.controller;

import com.galsie.gcs.homes.data.dto.addhome.request.AddHomeRequestDTO;
import com.galsie.gcs.homes.service.home.HomeService;
import com.galsie.gcs.homes.service.home.invites.HomeUserInviteService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeOnboardingController {

    @Autowired
    HomeService homeEntityService;

    @Autowired
    HomeUserInviteService homeUserInviteService;

    @Autowired
    HomeService homeService;

    @PostMapping("/onboarding/addHome")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> addHome(@RequestBody AddHomeRequestDTO addHomeRequestDTO) {
        return homeEntityService.addHome(addHomeRequestDTO).toResponseEntity();
    }

    @GetMapping("/onboarding/join/inviteCode")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> joinHome(@RequestParam("inviteUniqueCode") String inviteUniqueCode) {
       return homeUserInviteService.joinHome(inviteUniqueCode).toResponseEntity();
    }

}