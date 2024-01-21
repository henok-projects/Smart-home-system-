package com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetref.AppLangTranslationReferenceDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@GalDTO
public class GCSResponseErrorDTO {

    @NotNull
    @JsonProperty("error_type")
    GCSResponseErrorType errorType;

    @Nullable
    @JsonProperty("error_msg")
    String errorMsg;


    public static GCSResponseErrorDTO ofType(GCSResponseErrorType errorType){
        return new GCSResponseErrorDTO(errorType, errorType.getDefaultErrorMessage());
    }

    public static GCSResponseErrorDTO ofMessage(GCSResponseErrorType errorType, String message){
        return new GCSResponseErrorDTO(errorType, message);
    }

}
