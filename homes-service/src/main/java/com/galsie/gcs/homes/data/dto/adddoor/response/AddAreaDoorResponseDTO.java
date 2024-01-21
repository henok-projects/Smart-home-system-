package com.galsie.gcs.homes.data.dto.adddoor.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addarea.adddoor.AddAreaDoorResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.DoorDTO;
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
public class AddAreaDoorResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("add_area_door_response_error")
    @Nullable
    private AddAreaDoorResponseErrorType addAreaDoorResponseError;

    @JsonProperty("door_info")
    @Nullable
    private DoorDTO doorInfo;

    private static AddAreaDoorResponseDTO error(HomeResponseErrorType homeResponseErrorType, AddAreaDoorResponseErrorType addAreaDoorResponseErrorType) {
        return new AddAreaDoorResponseDTO(homeResponseErrorType, addAreaDoorResponseErrorType, null);
    }

    private static AddAreaDoorResponseDTO success(DoorDTO doorInfo) {
        return new AddAreaDoorResponseDTO(null, null, doorInfo);
    }

    public static GCSResponse<AddAreaDoorResponseDTO> responseError(HomeResponseErrorType homeResponseErrorType, AddAreaDoorResponseErrorType addAreaDoorResponseErrorType) {
        return GCSResponse.response(error(homeResponseErrorType, addAreaDoorResponseErrorType));
    }

    public static GCSResponse<AddAreaDoorResponseDTO> responseSuccess(DoorDTO doorInfo) {
        return GCSResponse.response(success(doorInfo));
    }

}
