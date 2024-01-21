package com.galsie.gcs.microservicecommon.lib.sms.data.dto.request;

import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Map;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SMSRequestDTO {

    @NotNull
    private SMSType smsType;

    @NotNull
    private short countryCode;

    @NotNull
    private String phoneNumber;

    @NotNull
    private Map<String, String> variableReplacement;
}
