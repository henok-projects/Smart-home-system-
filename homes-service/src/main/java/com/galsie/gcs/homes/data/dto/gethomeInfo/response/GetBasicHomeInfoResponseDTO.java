package com.galsie.gcs.homes.data.dto.gethomeInfo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.BasicHomeInfoDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;



@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetBasicHomeInfoResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("home_info")
    @Nullable
    private BasicHomeInfoDTO basicHomeInfoDTO;

    public static GetBasicHomeInfoResponseDTO error(HomeResponseErrorType homeResponseErrorType) {
        return new GetBasicHomeInfoResponseDTO(homeResponseErrorType, null);
    }

    public static GetBasicHomeInfoResponseDTO success(BasicHomeInfoDTO generalHomeInfoDTO) {
        return new GetBasicHomeInfoResponseDTO(null, generalHomeInfoDTO);
    }

    public static GCSResponse<GetBasicHomeInfoResponseDTO> responseError(HomeResponseErrorType homeResponseErrorType) {
        return GCSResponse.response(error(homeResponseErrorType));
    }

    public static GCSResponse<GetBasicHomeInfoResponseDTO> responseSuccess(BasicHomeInfoDTO generalHomeInfoDTO) {
        return GCSResponse.response(success(generalHomeInfoDTO));
    }

}


