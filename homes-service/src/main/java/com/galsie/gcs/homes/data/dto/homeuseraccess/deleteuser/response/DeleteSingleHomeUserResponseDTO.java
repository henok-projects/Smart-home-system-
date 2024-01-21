package com.galsie.gcs.homes.data.dto.homeuseraccess.deleteuser.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.deleteuser.DeleteSingleHomeUserResponseErrorType;
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
public class DeleteSingleHomeUserResponseDTO {

    @JsonProperty("home_user_id")
    @Nullable
    private Long homeUserId;

    @JsonProperty("delete_single_home_user_response_error")
    @Nullable
    private DeleteSingleHomeUserResponseErrorType deleteSingleHomeUserResponseError;


    public static DeleteSingleHomeUserResponseDTO error(DeleteSingleHomeUserResponseErrorType deleteSingleHomeUserResponseErrorType) {
        return new DeleteSingleHomeUserResponseDTO( null, deleteSingleHomeUserResponseErrorType);
    }

    public static DeleteSingleHomeUserResponseDTO success(Long homeUserId) {
        return new DeleteSingleHomeUserResponseDTO(homeUserId,null);
    }


    public static GCSResponse<DeleteSingleHomeUserResponseDTO> responseError(DeleteSingleHomeUserResponseErrorType deleteSingleHomeUserResponseErrorType) {
        return GCSResponse.response(error(deleteSingleHomeUserResponseErrorType));
    }


    public static GCSResponse<DeleteSingleHomeUserResponseDTO> responseSuccess(DeleteSingleHomeUserResponseDTO deleteSingleHomeUserResponseDTO) {
        return GCSResponse.response(success(deleteSingleHomeUserResponseDTO.getHomeUserId()));
    }

}
