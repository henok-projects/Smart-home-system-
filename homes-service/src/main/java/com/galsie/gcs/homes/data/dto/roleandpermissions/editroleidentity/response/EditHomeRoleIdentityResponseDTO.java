package com.galsie.gcs.homes.data.dto.roleandpermissions.editroleidentity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.editroleidentity.EditHomeRoleIdentityResponseErrorType;
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
public class EditHomeRoleIdentityResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("edit_role_identity_response_error")
    @Nullable
    private EditHomeRoleIdentityResponseErrorType editRoleIdentityResponseError;



    public static EditHomeRoleIdentityResponseDTO error(HomeResponseErrorType homeResponseError, EditHomeRoleIdentityResponseErrorType editRoleIdentityResponseError) {
        return new EditHomeRoleIdentityResponseDTO(homeResponseError, editRoleIdentityResponseError);
    }

    public static EditHomeRoleIdentityResponseDTO success() {
        return new EditHomeRoleIdentityResponseDTO();
    }

    public static GCSResponse<EditHomeRoleIdentityResponseDTO> responseError(HomeResponseErrorType homeResponseError, EditHomeRoleIdentityResponseErrorType editRoleIdentityResponseError) {
        return GCSResponse.response(error(homeResponseError, editRoleIdentityResponseError));
    }

    public static GCSResponse<EditHomeRoleIdentityResponseDTO> responseSuccess() {
        return GCSResponse.response(success());
    }

}
