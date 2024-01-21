package com.galsie.gcs.homes.data.dto.addwindow.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addarea.addwindow.AddAreaWindowResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.WindowDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.utils.lang.Nullable;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddAreaWindowResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("add_area_window_response_error")
    @Nullable
    private AddAreaWindowResponseErrorType addAreaWindowResponseError;

    @JsonProperty("window_info")
    @Nullable
    private WindowDTO windowInfo;

    private static AddAreaWindowResponseDTO error(HomeResponseErrorType homeResponseErrorType, AddAreaWindowResponseErrorType addAreaWindowResponseErrorType) {
        return new AddAreaWindowResponseDTO(homeResponseErrorType, addAreaWindowResponseErrorType, null);
    }

    private static AddAreaWindowResponseDTO success(WindowDTO windowInfo) {
        return new AddAreaWindowResponseDTO(null, null, windowInfo);
    }

    public static GCSResponse<AddAreaWindowResponseDTO> responseError(HomeResponseErrorType homeResponseErrorType, AddAreaWindowResponseErrorType addAreaWindowResponseErrorType) {
        return GCSResponse.response(error(homeResponseErrorType, addAreaWindowResponseErrorType));
    }

    public static GCSResponse<AddAreaWindowResponseDTO> responseSuccess(WindowDTO windowInfo) {
        return GCSResponse.response(success(windowInfo));
    }


}
