package com.galsie.gcs.homes.data.dto.roleandpermissions.addusertorole.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addusertorole.AddSingleUserToRoleResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addusertorole.AddUserListToRoleResponseErrorType;
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
public class AddUserListToRoleResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @Nullable
    @JsonProperty("add_users_to_role_response_error")
    private AddUserListToRoleResponseErrorType addUserToRoleResponseError;

    @Nullable
    @JsonProperty("add_single_user_to_role_errors")
    private List<AddSingleUserToRoleResponseErrorType> addSingleUserToRoleErrors;



    public static AddUserListToRoleResponseDTO error(HomeResponseErrorType homeResponseError, AddUserListToRoleResponseErrorType addUserToRoleResponseError) {
        return new AddUserListToRoleResponseDTO(homeResponseError, addUserToRoleResponseError, null);
    }

    public static AddUserListToRoleResponseDTO success(List<AddSingleUserToRoleResponseErrorType> addSingleUserToRoleErrors) {
        return new AddUserListToRoleResponseDTO(null, null, addSingleUserToRoleErrors);
    }

    public static GCSResponse<AddUserListToRoleResponseDTO> responseError(HomeResponseErrorType homeResponseError, AddUserListToRoleResponseErrorType addUserToRoleResponseError) {
        return GCSResponse.response(error(homeResponseError, addUserToRoleResponseError));
    }

    public static GCSResponse<AddUserListToRoleResponseDTO> responseSuccess(List<AddSingleUserToRoleResponseErrorType> addSingleUserToRoleErrors) {
        return GCSResponse.response(success(addSingleUserToRoleErrors));
    }

}


