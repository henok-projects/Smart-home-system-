package com.galsie.gcs.homes.data.dto.roleandpermissions.deleterolefromhome.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.deleterolefromhome.DeleteHomeRoleResponseErrorType;
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
public class DeleteHomeRoleResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("remove_role_response_error")
    @Nullable
    private DeleteHomeRoleResponseErrorType deleteHomeRoleResponseErrorType;


    public static DeleteHomeRoleResponseDTO error(HomeResponseErrorType homeResponseError, DeleteHomeRoleResponseErrorType deleteHomeRoleResponseErrorType) {
        return new DeleteHomeRoleResponseDTO(homeResponseError, deleteHomeRoleResponseErrorType);
    }
    public static DeleteHomeRoleResponseDTO success() {
        return new DeleteHomeRoleResponseDTO();
    }

    public static GCSResponse<DeleteHomeRoleResponseDTO> responseError(HomeResponseErrorType homeResponseError, DeleteHomeRoleResponseErrorType deleteHomeRoleResponseErrorType) {
        return GCSResponse.response(error(homeResponseError, deleteHomeRoleResponseErrorType));
    }

    public static GCSResponse<DeleteHomeRoleResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }

}
