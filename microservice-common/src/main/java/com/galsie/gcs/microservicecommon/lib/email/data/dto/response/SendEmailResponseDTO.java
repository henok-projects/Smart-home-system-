package com.galsie.gcs.microservicecommon.lib.email.data.dto.response;

import com.galsie.gcs.microservicecommon.lib.email.data.discrete.SendEmailResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendEmailResponseDTO {

    @Nullable
    SendEmailResponseErrorType sendEmailResponseError;

    public boolean hasError(){
        return sendEmailResponseError != null;
    }

    public static GCSResponse<SendEmailResponseDTO> responseError(SendEmailResponseErrorType sendEmailResponseError){
        return GCSResponse.response(new SendEmailResponseDTO(sendEmailResponseError));
    }

    public static GCSResponse<SendEmailResponseDTO> responseSuccess(){
        return GCSResponse.response(new SendEmailResponseDTO(null));
    }


}
