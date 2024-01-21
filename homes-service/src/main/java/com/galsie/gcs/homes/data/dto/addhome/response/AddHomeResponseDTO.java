package com.galsie.gcs.homes.data.dto.addhome.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.addhome.AddHomeResponseErrorType;
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
public class AddHomeResponseDTO {

    @JsonProperty("add_home_response_error")
    @Nullable
    private AddHomeResponseErrorType addHomeResponseError;

    @JsonProperty("home_info")
    @Nullable
    private BasicHomeInfoDTO basicHomeInfoDTO;


    public static AddHomeResponseDTO error(AddHomeResponseErrorType addHomeResponseErrorType) {
        return new AddHomeResponseDTO(addHomeResponseErrorType, null);
    }

    public static AddHomeResponseDTO success(BasicHomeInfoDTO homeInfo) {
        return new AddHomeResponseDTO(null, homeInfo);
    }

    public static GCSResponse<AddHomeResponseDTO> responseError(AddHomeResponseErrorType addHomeResponseErrorType) {
        return GCSResponse.response(error(addHomeResponseErrorType));
    }

    public static GCSResponse<AddHomeResponseDTO> responseSuccess(BasicHomeInfoDTO homeInfo) {
        return GCSResponse.response(success(homeInfo));
    }

}
