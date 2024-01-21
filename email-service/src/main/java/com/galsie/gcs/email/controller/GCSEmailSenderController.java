package com.galsie.gcs.email.controller;

import com.galsie.gcs.microservicecommon.lib.email.data.dto.request.SendEmailRequestDTO;
import com.galsie.gcs.email.service.GCSEmailSenderService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sender")
public class GCSEmailSenderController {

    @Autowired
    GCSEmailSenderService emailSenderService;

    @AuthenticatedGCSRequest(authSessionTypes = {GalSecurityAuthSessionType.GCS_MICROSERVICE})
    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody SendEmailRequestDTO sendEmailRequestDTO){
        return emailSenderService.sendEmail(sendEmailRequestDTO).toResponseEntity();
    }

}
