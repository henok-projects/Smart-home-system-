package com.galsie.gcs.users.controller.editprofileinfo;

import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.users.data.dto.editprofileinfo.dateofbirth.request.EditUserBirthdateRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.email.request.EditUserEmailRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.fullname.request.EditUserFullNameRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.gender.request.EditUserGenderRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.password.request.EditUserPasswordRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.phone.request.EditUserPhoneRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.username.request.EditUserUsernameRequestDTO;
import com.galsie.gcs.users.service.editprofileinfo.EditUserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/edit")
@Slf4j
public class EditUserProfileController {
    @Autowired
    EditUserProfileService userProfileService;

    @PostMapping("/email")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editEmail(@RequestBody EditUserEmailRequestDTO request) {
        return userProfileService.requestEditEmail(request).toResponseEntity();
    }

    @PostMapping("/phone")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editPhone(@RequestBody EditUserPhoneRequestDTO request) {
        return userProfileService.requestEditPhone(request).toResponseEntity();
    }

    @PostMapping("/username")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editUsername(@RequestBody EditUserUsernameRequestDTO request) {
        return userProfileService.requestEditUsername(request).toResponseEntity();
    }

    @PostMapping("/fullname")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editFullName(@RequestBody EditUserFullNameRequestDTO request) {
        return userProfileService.requestEditFullName(request).toResponseEntity();
    }
    @PostMapping("/birthdate")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editBirthdate(@RequestBody EditUserBirthdateRequestDTO request) {
        return userProfileService.requestEditBirthdate(request).toResponseEntity();
    }
    @PostMapping("/gender")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editGender(@RequestBody EditUserGenderRequestDTO request) {
        return userProfileService.requestEditGender(request).toResponseEntity();
    }

    @PostMapping("/password")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editPassword(@RequestBody EditUserPasswordRequestDTO request) {
        return userProfileService.requestEditPassword(request).toResponseEntity();
    }
}

