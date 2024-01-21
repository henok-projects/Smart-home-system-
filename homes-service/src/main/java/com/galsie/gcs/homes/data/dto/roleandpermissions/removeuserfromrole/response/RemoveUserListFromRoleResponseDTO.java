package com.galsie.gcs.homes.data.dto.roleandpermissions.removeuserfromrole.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.deleteuserfromrole.RemoveSingleUserFromRoleResponseErrorType;
import com.galsie.gcs.homes.data.discrete.deleteuserfromrole.RemoveUserListFromRoleResponseErrorType;
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
public class RemoveUserListFromRoleResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("remove_users_from_role_response_error")
    @Nullable
    private RemoveUserListFromRoleResponseErrorType removeUsersFromRoleResponseError;

    @JsonProperty("remove_single_user_from_role_errors")
    @Nullable
    private List<RemoveSingleUserFromRoleResponseErrorType> removeSingleUserFromRoleErrors;


    public static RemoveUserListFromRoleResponseDTO error(HomeResponseErrorType homeResponseError, RemoveUserListFromRoleResponseErrorType removeUsersFromRoleResponseError) {
        return new RemoveUserListFromRoleResponseDTO(homeResponseError, removeUsersFromRoleResponseError, null);
    }
    public static RemoveUserListFromRoleResponseDTO success(List<RemoveSingleUserFromRoleResponseErrorType> removeSingleUserFromRoleErrors) {
        return new RemoveUserListFromRoleResponseDTO(null, null, removeSingleUserFromRoleErrors);
    }

    public static GCSResponse<RemoveUserListFromRoleResponseDTO> responseError(HomeResponseErrorType homeResponseError, RemoveUserListFromRoleResponseErrorType removeUsersFromRoleResponseError) {
        return GCSResponse.response(error(homeResponseError, removeUsersFromRoleResponseError));
    }

    public static GCSResponse<RemoveUserListFromRoleResponseDTO> responseSuccess(List<RemoveSingleUserFromRoleResponseErrorType> removeSingleUserFromRoleErrors) {
        return GCSResponse.response(success(removeSingleUserFromRoleErrors));
    }

}

