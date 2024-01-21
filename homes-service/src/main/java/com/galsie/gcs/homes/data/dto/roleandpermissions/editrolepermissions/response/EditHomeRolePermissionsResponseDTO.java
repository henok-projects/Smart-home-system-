package com.galsie.gcs.homes.data.dto.roleandpermissions.editrolepermissions.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.editrolepermission.EditHomeRolePermissionsResponseErrorType;
import com.galsie.gcs.homes.data.dto.roleandpermissions.common.RoleInfoDTO;
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
public class EditHomeRolePermissionsResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("edit_role_permissions_response_error")
    @Nullable
    private EditHomeRolePermissionsResponseErrorType editRolePermissionsResponseError;

    @JsonProperty("role_info")
    @Nullable
    private RoleInfoDTO roleInfo;


    public static EditHomeRolePermissionsResponseDTO error(HomeResponseErrorType homeResponseError,EditHomeRolePermissionsResponseErrorType editRolePermissionsResponseError) {
        return new EditHomeRolePermissionsResponseDTO(homeResponseError, editRolePermissionsResponseError, null);
    }

    public static EditHomeRolePermissionsResponseDTO success(RoleInfoDTO roleInfo) {
        return new EditHomeRolePermissionsResponseDTO(null, null, roleInfo);
    }

    public static GCSResponse<EditHomeRolePermissionsResponseDTO> responseError(HomeResponseErrorType homeResponseError, EditHomeRolePermissionsResponseErrorType editRolePermissionsResponseError) {
        return GCSResponse.response(error(homeResponseError, editRolePermissionsResponseError));
    }

    public static GCSResponse<EditHomeRolePermissionsResponseDTO> responseSuccess(EditHomeRolePermissionsResponseDTO editHomeRolePermissionsResponseDTO) {
        return GCSResponse.response(success(editHomeRolePermissionsResponseDTO.getRoleInfo()));
    }

}
