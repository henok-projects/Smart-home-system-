package com.galsie.gcs.smsservice.controller;


import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSBatchRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSRequestDTO;
import com.galsie.gcs.smsservice.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/smsSender")
public class SMSController {

    @Autowired
    private SmsService smsService;

    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.GCS_MICROSERVICE)
    @PostMapping("/sendSms")
    public ResponseEntity<?> sendSMS(@RequestBody SMSRequestDTO smsRequest){
        return smsService.sendSMS(smsRequest).toResponseEntity();
    }

    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.GCS_MICROSERVICE)
    @PostMapping("/sendBatchSms")
    public ResponseEntity<?> sendBatchSMS(@RequestBody SMSBatchRequestDTO smsRequest){
        return smsService.sendBatchSMS(smsRequest).toResponseEntity();
    }

}
