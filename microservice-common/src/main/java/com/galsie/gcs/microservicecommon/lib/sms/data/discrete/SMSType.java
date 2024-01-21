package com.galsie.gcs.microservicecommon.lib.sms.data.discrete;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SMSType {

    OTP_SMS_VERIFICATION(0,"Galsie User Management"),
    USER_SMS_VERIFICATION(1, "Galsie");

    private final int id;
    final String fromName;
}
