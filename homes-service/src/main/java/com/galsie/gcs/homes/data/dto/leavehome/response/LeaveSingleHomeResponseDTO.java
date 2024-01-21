package com.galsie.gcs.homes.data.dto.leavehome.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.leavehome.LeaveHomeResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LeaveSingleHomeResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("leave_home_response_error")
    @Nullable
    private LeaveHomeResponseErrorType leaveHomeResponseError;

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;


    public static LeaveSingleHomeResponseDTO error(HomeResponseErrorType homeResponseError, LeaveHomeResponseErrorType leaveHomeResponseErrorType, Long homeId) {
        return new LeaveSingleHomeResponseDTO(homeResponseError, leaveHomeResponseErrorType, homeId);
    }

    public static LeaveSingleHomeResponseDTO success(Long homeId) {
        return new LeaveSingleHomeResponseDTO(null, null,  homeId);
    }

    public static GCSResponse<LeaveSingleHomeResponseDTO> responseError(HomeResponseErrorType homeResponseError, LeaveHomeResponseErrorType leaveHomeResponseErrorType, Long homeId) {
        return GCSResponse.response(error(homeResponseError, leaveHomeResponseErrorType, homeId));
    }

    public static GCSResponse<LeaveSingleHomeResponseDTO> responseSuccess(Long homeId) {
        return GCSResponse.response(success(homeId));
    }
}
