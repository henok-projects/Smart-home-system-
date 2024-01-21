package com.galsie.gcs.homes.data.dto.archivehome.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
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
public class ArchiveSingleHomeResponseDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;


    public static ArchiveSingleHomeResponseDTO error(Long homeId, HomeResponseErrorType homeResponseErrorType) {
        return new ArchiveSingleHomeResponseDTO( homeId, homeResponseErrorType);
    }

    public static ArchiveSingleHomeResponseDTO success(Long homeId) {
        return new ArchiveSingleHomeResponseDTO(homeId,null);
    }


    public static GCSResponse<ArchiveSingleHomeResponseDTO> responseError(Long homeId,HomeResponseErrorType homeResponseError) {
        return GCSResponse.response(error(homeId, homeResponseError));
    }


    public static GCSResponse<ArchiveSingleHomeResponseDTO> responseSuccess(ArchiveSingleHomeResponseDTO homeArchiveResponse) {
        return GCSResponse.response(success(homeArchiveResponse.getHomeId()));
    }
}
