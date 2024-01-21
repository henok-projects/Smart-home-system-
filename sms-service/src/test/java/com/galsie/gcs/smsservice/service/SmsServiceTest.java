package com.galsie.gcs.smsservice.service;

import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSType;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class SmsServiceTest {

    @Autowired
    SmsService smsService;

    @Test
    void sendSMS() {
        Map<String, String> variableReplacement = new HashMap<>();
        variableReplacement.put("verification_code", "254333");
        SMSRequestDTO requestDTO = SMSRequestDTO.builder().smsType(SMSType.USER_SMS_VERIFICATION)
                .phoneNumber("+96171842007" ).variableReplacement(variableReplacement).build();
        var response = smsService.sendSMS(requestDTO);
        Assertions.assertNotNull(response.getResponseData().getError());
    }
}