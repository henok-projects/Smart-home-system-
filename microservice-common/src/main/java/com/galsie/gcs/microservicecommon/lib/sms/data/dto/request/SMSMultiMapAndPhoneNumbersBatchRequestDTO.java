package com.galsie.gcs.microservicecommon.lib.sms.data.dto.request;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

/**
 * A child class of {@link SMSBatchRequestDTO} used in scenarios where the destination phone numbers, sms type and
 * variable replacement map are all different
 */
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SMSMultiMapAndPhoneNumbersBatchRequestDTO implements SMSBatchRequestDTO{

    @NotNull
    List<SMSRequestDTO> smsRequestDTOList;

}
