package com.galsie.gcs.microservicecommon.lib.sms.data.dto.response;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.Nullable;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendSMSResponseDTO {
    @Nullable
    private SMSErrorType error;

    public boolean hasError(){
        return error != null;
    }

    private static SendSMSResponseDTO success(){
        return  SendSMSResponseDTO.builder().build();
    }

    private static SendSMSResponseDTO error(SMSErrorType error){
        return  SendSMSResponseDTO.builder().error(error).build();
    }

    public static GCSResponse<SendSMSResponseDTO> successResponse(){
        return GCSResponse.response(success());
    }

    public static GCSResponse<SendSMSResponseDTO> errorResponse(SMSErrorType error){
        return GCSResponse.response(error(error));
    }
}
