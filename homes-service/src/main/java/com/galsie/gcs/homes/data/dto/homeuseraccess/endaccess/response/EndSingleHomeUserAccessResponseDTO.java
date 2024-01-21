package com.galsie.gcs.homes.data.dto.homeuseraccess.endaccess.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.endaccess.EndSingleUserHomeAccessResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
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
public class EndSingleHomeUserAccessResponseDTO {

    @JsonProperty("end_single_home_user_access_response_error")
    @Nullable
    private EndSingleUserHomeAccessResponseErrorType endSingleHomeUserAccessResponseError;

    @JsonProperty("home_user_id")
    @Nullable
    private Long homeUserId;

    @JsonProperty("access_info")
    @NotNull
    private UserHomeAccessInfoDTO accessInfo;


    public static EndSingleHomeUserAccessResponseDTO error(EndSingleUserHomeAccessResponseErrorType endSingleUserHomeAccessResponseErrorType) {
        return new EndSingleHomeUserAccessResponseDTO(endSingleUserHomeAccessResponseErrorType,null, null);
    }

    public static EndSingleHomeUserAccessResponseDTO success(Long homeUserId, UserHomeAccessInfoDTO accessInfo) {
        return new EndSingleHomeUserAccessResponseDTO(null, homeUserId, accessInfo);
    }


    public static GCSResponse<EndSingleHomeUserAccessResponseDTO> responseError(EndSingleUserHomeAccessResponseErrorType endSingleUserHomeAccessResponseErrorType) {
        return GCSResponse.response(error(endSingleUserHomeAccessResponseErrorType));
    }

    public static GCSResponse<EndSingleHomeUserAccessResponseDTO> responseSuccess(EndSingleHomeUserAccessResponseDTO endSingleHomeUserAccessResponseDTO) {
        return GCSResponse.response(success(endSingleHomeUserAccessResponseDTO.getHomeUserId(), endSingleHomeUserAccessResponseDTO.getAccessInfo()));
    }

}
