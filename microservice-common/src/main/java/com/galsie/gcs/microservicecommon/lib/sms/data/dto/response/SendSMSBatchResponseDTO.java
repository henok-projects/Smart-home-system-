package com.galsie.gcs.microservicecommon.lib.sms.data.dto.response;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.Nullable;
import lombok.*;

import java.util.Map;

/**
 * This DTO holds a map of Integer to SmsErrorType, where the Integer is the index of the SMSRequestDTO in {@link com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSMultiMapAndPhoneNumbersBatchRequestDTO#getSmsRequestDTOList()}
 * or the phonenumber in {@link com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSOneMapMultiplePhoneNumbersBatchRequestDTO#getPhoneNumberList()}.
 * If the index is -1, then the error is a batch error in {@link com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSOneMapMultiplePhoneNumbersBatchRequestDTO}.
 */
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendSMSBatchResponseDTO {

    @Nullable
    private Map<Integer, SMSErrorType> errors;

    public boolean hasError(){
        return errors != null;
    }

    private static SendSMSBatchResponseDTO success(){
        return  SendSMSBatchResponseDTO.builder().build();
    }

    private static SendSMSBatchResponseDTO error(Map<Integer, SMSErrorType> errors){
        return SendSMSBatchResponseDTO.builder().errors(errors).build();
    }

    public static GCSResponse<SendSMSBatchResponseDTO> responseSuccess(){
        return GCSResponse.response(success());
    }


    public static GCSResponse<SendSMSBatchResponseDTO> responseError(Map<Integer, SMSErrorType> errors){
        return GCSResponse.response(error(errors));
    }

}
