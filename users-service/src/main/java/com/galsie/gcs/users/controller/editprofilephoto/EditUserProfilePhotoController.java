package com.galsie.gcs.users.controller.editprofilephoto;

import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.users.data.dto.editprofilephoto.request.EditUserProfilePhotoRequestDTO;
import com.galsie.gcs.users.service.EditUserProfilePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/account/edit")
public class EditUserProfilePhotoController {
    @Autowired
    EditUserProfilePhotoService editUserProfilePhotoService;

    @PostMapping("/profilePhoto")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editProfilePhoto(@RequestBody EditUserProfilePhotoRequestDTO request) {
        return editUserProfilePhotoService.requestUpdateUserProfilePhoto(request).toResponseEntity();
    }
    @PostMapping("/removePhoto")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> removeProfilePhoto() {
        return editUserProfilePhotoService.requestRemoveProfilePhoto().toResponseEntity();
    }
}
