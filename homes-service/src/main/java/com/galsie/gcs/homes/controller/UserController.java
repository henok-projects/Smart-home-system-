package com.galsie.gcs.homes.controller;

import com.galsie.gcs.homes.service.home.invites.HomeUserInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/home")
public class UserController {

    @Autowired
    HomeUserInviteService homeUserInviteService;

    @GetMapping("/user/getReceivedInvites")
    public ResponseEntity<?> getReceivedInviteList(){
        return homeUserInviteService.getReceivedInvites().toResponseEntity();
    }

}
