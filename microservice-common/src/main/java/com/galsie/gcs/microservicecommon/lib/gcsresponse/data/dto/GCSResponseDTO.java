package com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@GalDTO
public class GCSResponseDTO<responseDataType> {

    @Nullable
    @JsonProperty("gcs_error")
    GCSResponseErrorDTO error;

    @Nullable
    @JsonProperty("response_data")
    responseDataType responseData;

    public boolean hasError(){
        return this.error != null;
    }


}
