package com.galsie.gcs.smsservice.service;


import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSBatchRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.response.SendSMSBatchResponseDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.response.SendSMSResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;

public interface SmsService {
    GCSResponse<SendSMSResponseDTO> sendSMS(SMSRequestDTO smsRequest);

    GCSResponse<SendSMSBatchResponseDTO> sendBatchSMS(SMSBatchRequestDTO smsBatchRequest);

}
