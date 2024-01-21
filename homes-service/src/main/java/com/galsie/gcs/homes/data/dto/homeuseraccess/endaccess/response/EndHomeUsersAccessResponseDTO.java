package com.galsie.gcs.homes.data.dto.homeuseraccess.endaccess.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.endaccess.EndHomeUsersAccessResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EndHomeUsersAccessResponseDTO {

    @JsonProperty("end_home_users_access_response_error")
    @Nullable
    private EndHomeUsersAccessResponseErrorType endHomeUsersAccessResponseError;

    @JsonProperty("end_home_user_access_responses")
    @Nullable
    private List<EndSingleHomeUserAccessResponseDTO> endHomeUserAccessResponses;


    public static EndHomeUsersAccessResponseDTO error(EndHomeUsersAccessResponseErrorType endHomeUsersAccessResponseErrorType) {
        return new EndHomeUsersAccessResponseDTO(endHomeUsersAccessResponseErrorType, null);
    }

    public static EndHomeUsersAccessResponseDTO success(List<EndSingleHomeUserAccessResponseDTO> endSingleHomeUserAccessResponseDTOList) {
        return new EndHomeUsersAccessResponseDTO(null, endSingleHomeUserAccessResponseDTOList);
    }

    public static GCSResponse<EndHomeUsersAccessResponseDTO> responseError(EndHomeUsersAccessResponseErrorType endHomeUsersAccessResponseErrorType) {
        return GCSResponse.response(error(endHomeUsersAccessResponseErrorType));
    }

    public static GCSResponse<EndHomeUsersAccessResponseDTO> responseSuccess(EndHomeUsersAccessResponseDTO endHomeUsersAccessResponseDTO) {
        return GCSResponse.response(success(endHomeUsersAccessResponseDTO.getEndHomeUserAccessResponses()));
    }
}
