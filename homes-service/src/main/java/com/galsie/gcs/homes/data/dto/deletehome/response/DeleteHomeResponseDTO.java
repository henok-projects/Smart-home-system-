package com.galsie.gcs.homes.data.dto.deletehome.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.deletehome.DeleteHomeResponseErrorType;
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
public class DeleteHomeResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("delete_home_response_error")
    @Nullable
    DeleteHomeResponseErrorType deleteHomeResponseError;

    public static DeleteHomeResponseDTO error(HomeResponseErrorType homeResponseErrorType, DeleteHomeResponseErrorType deleteHomeResponseErrorType) {
        return new DeleteHomeResponseDTO(homeResponseErrorType, deleteHomeResponseErrorType);
    }
    public static DeleteHomeResponseDTO success() {
        return new DeleteHomeResponseDTO(null, null);
    }

    public static GCSResponse<DeleteHomeResponseDTO> responseError(HomeResponseErrorType homeResponseErrorType, DeleteHomeResponseErrorType deleteHomeResponseErrorType) {
        return GCSResponse.response(error(homeResponseErrorType, deleteHomeResponseErrorType));
    }

    public static GCSResponse<DeleteHomeResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }
}
