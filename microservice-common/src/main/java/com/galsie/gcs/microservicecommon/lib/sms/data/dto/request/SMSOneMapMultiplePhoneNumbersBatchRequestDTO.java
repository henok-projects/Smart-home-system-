package com.galsie.gcs.microservicecommon.lib.sms.data.dto.request;

import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.utils.pair.Pair;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * A child class of {@link SMSBatchRequestDTO} used in scenarios where the destination phone numbers are different but the sms type and
 * variable replacement map are the same
 */
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SMSOneMapMultiplePhoneNumbersBatchRequestDTO implements SMSBatchRequestDTO{

    @NotNull
    SMSType smsType;

    @NotNull
    List<Pair<Short, String>> phoneNumberList;

    @NotNull
    Map<String, String> variableReplacementMap;


}
