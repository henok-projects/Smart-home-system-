package com.galsie.gcs.users.controller.contactinfo;

import com.galsie.gcs.users.data.dto.getcontactinfo.request.many.GetManyUserContactInfoRequestDTO;
import com.galsie.gcs.users.data.dto.getcontactinfo.request.single.GetUserContactInfoRequestDTO;
import com.galsie.gcs.users.service.UserContactInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserContactInfoController {

    @Autowired
    private UserContactInfoService userContactInfoService;

    @PostMapping("/getContactInfo/one")
    public ResponseEntity<?> getUserContactInfo(@RequestBody GetUserContactInfoRequestDTO request) {
        return userContactInfoService.requestGetUserContactInfo(request).toResponseEntity();
    }

    @PostMapping("/getContactInfo/many")
    public ResponseEntity<?> getManyUserContactInfo(@RequestBody GetManyUserContactInfoRequestDTO request) {
        return userContactInfoService.requestGetManyUserContactInfo(request).toResponseEntity();
    }
}

