package com.galsie.gcs.microservicecommon.lib.sms.data.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.galsie.gcs.microservicecommon.lib.deserialize.sms.DeserializerSMSBatchRequestDTO;

/**
 * The parent class for {@link SMSOneMapMultiplePhoneNumbersBatchRequestDTO} and {@link SMSMultiMapAndPhoneNumbersBatchRequestDTO}
 * used for making batch requests to the SMS microservice.
 */
@JsonDeserialize(using = DeserializerSMSBatchRequestDTO.class)
public interface SMSBatchRequestDTO {
}
