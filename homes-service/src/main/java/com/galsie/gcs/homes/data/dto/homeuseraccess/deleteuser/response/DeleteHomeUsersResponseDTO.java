package com.galsie.gcs.homes.data.dto.homeuseraccess.deleteuser.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.deleteuser.DeleteHomeUsersResponseErrorType;
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
public class DeleteHomeUsersResponseDTO {

    @JsonProperty("delete_home_users_response_error")
    @Nullable
    private DeleteHomeUsersResponseErrorType deleteHomeUsersResponseError;

    @JsonProperty("delete_home_user_responses")
    @Nullable
    private List<DeleteSingleHomeUserResponseDTO> deleteHomeUserResponses;



    public static DeleteHomeUsersResponseDTO error(DeleteHomeUsersResponseErrorType deleteHomeUsersResponseError) {
        return new DeleteHomeUsersResponseDTO(deleteHomeUsersResponseError, null);
    }

    public static DeleteHomeUsersResponseDTO success(List<DeleteSingleHomeUserResponseDTO> deleteSingleHomeUserResponseDTOList) {
        return new DeleteHomeUsersResponseDTO(null, deleteSingleHomeUserResponseDTOList);
    }

    public static GCSResponse<DeleteHomeUsersResponseDTO> responseError(DeleteHomeUsersResponseErrorType deleteHomeUsersResponseError) {
        return GCSResponse.response(error(deleteHomeUsersResponseError));
    }

    public static GCSResponse<DeleteHomeUsersResponseDTO> responseSuccess(DeleteHomeUsersResponseDTO deleteHomeUsersResponseDTO) {
        return GCSResponse.response(success(deleteHomeUsersResponseDTO.getDeleteHomeUserResponses()));
    }
}
