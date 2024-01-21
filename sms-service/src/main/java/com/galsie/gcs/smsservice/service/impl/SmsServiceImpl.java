package com.galsie.gcs.smsservice.service.impl;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSErrorType;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSBatchRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSMultiMapAndPhoneNumbersBatchRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSOneMapMultiplePhoneNumbersBatchRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.response.SendSMSBatchResponseDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.response.SendSMSResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.smsservice.sms.GCSSmsSender;
import com.galsie.gcs.smsservice.sms.SMSTxtModelManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galsie.gcs.smsservice.service.SmsService;

import java.util.HashMap;


@Service
public class SmsServiceImpl implements SmsService {


    @Autowired
    GCSSmsSender smsSender;

    @Autowired
    SMSTxtModelManager SMSTxtModelManager;


    @Override
    public GCSResponse<SendSMSResponseDTO> sendSMS(SMSRequestDTO smsRequest) {
        var smsType = smsRequest.getSmsType();
        var modelOpt = SMSTxtModelManager.getSmsModelFor(smsType);
        if (modelOpt.isEmpty()){
            return SendSMSResponseDTO.errorResponse(SMSErrorType.SMS_TXT_MODEL_NOT_FOUND);
        }
        var txtText  = modelOpt.get().getReplacedTXT(smsRequest.getVariableReplacement());
        var fullPhoneNumber = "+" + smsRequest.getCountryCode() + smsRequest.getPhoneNumber();
        var successful = smsSender.sendSMS(fullPhoneNumber, txtText);
        if(successful){
            return SendSMSResponseDTO.successResponse();
        }
        return SendSMSResponseDTO.errorResponse(SMSErrorType.SMS_SENDER_FAILED);
    }

    @Override
    public GCSResponse<SendSMSBatchResponseDTO> sendBatchSMS(SMSBatchRequestDTO smsBatchRequest) {
        if(smsBatchRequest instanceof SMSMultiMapAndPhoneNumbersBatchRequestDTO multiVariableRequest){
            return gcsInternalSendMultipleMapMultipleNumbers(multiVariableRequest);
        }if(smsBatchRequest instanceof SMSOneMapMultiplePhoneNumbersBatchRequestDTO oneVariableRequest){
            return gcsInternalSendOneMapMultipleNumbers(oneVariableRequest);
        }
        return GCSResponse.errorResponseWithMessage(GCSResponseErrorType.MISMATCH_ERROR, "Mismatch between the expected DTOs and the passed one.");
    }

    private GCSResponse<SendSMSBatchResponseDTO> gcsInternalSendMultipleMapMultipleNumbers(SMSMultiMapAndPhoneNumbersBatchRequestDTO smsBatchRequest){
        var smsRequestDTOList = smsBatchRequest.getSmsRequestDTOList();
        var errors = new HashMap<Integer, SMSErrorType>();
        for(var smsRequestDTO : smsRequestDTOList){
            var responseData = sendSMS(smsRequestDTO).getResponseData();
            if(responseData.hasError()){
                errors.put(smsRequestDTOList.indexOf(smsRequestDTO), responseData.getError());
            }
        }
        if(errors.isEmpty()){
            return SendSMSBatchResponseDTO.responseSuccess();
        }
        return SendSMSBatchResponseDTO.responseError(errors);
    }

    private GCSResponse<SendSMSBatchResponseDTO> gcsInternalSendOneMapMultipleNumbers(SMSOneMapMultiplePhoneNumbersBatchRequestDTO smsBatchRequest){
        var modelOpt = SMSTxtModelManager.getSmsModelFor(smsBatchRequest.getSmsType());
        var errors = new HashMap<Integer, SMSErrorType>();
        if (modelOpt.isEmpty()){
            errors.put(-1, SMSErrorType.SMS_TXT_MODEL_NOT_FOUND);
            return SendSMSBatchResponseDTO.responseError(errors);
        }
        var txtText  = modelOpt.get().getReplacedTXT(smsBatchRequest.getVariableReplacementMap());
        for(var phoneNumber : smsBatchRequest.getPhoneNumberList()) {
            var successful = smsSender.sendSMS("+" +phoneNumber.getFirst() + phoneNumber.getSecond(), txtText);
            if(!successful){
                errors.put(smsBatchRequest.getPhoneNumberList().indexOf(phoneNumber), SMSErrorType.SMS_SENDER_FAILED);
            }
        }
        if(errors.isEmpty()){
            return SendSMSBatchResponseDTO.responseSuccess();
        }
        return SendSMSBatchResponseDTO.responseError(errors);
    }

}
