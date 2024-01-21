package com.galsie.gcs.homes.data.dto.roleandpermissions.addusertorole.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.addusertorole.AddUserListToRoleResponseErrorType;
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
public class AddUserToRoleResponseDTO {
    @JsonProperty("add_user_to_role_response_error")
    @Nullable
    private AddUserListToRoleResponseErrorType addUserListToRoleResponseErrorType;

    @JsonProperty("role_user_info")
    @Nullable
    private RoleUserInfoDTO roleUserInfo;


    public static AddUserToRoleResponseDTO error(AddUserListToRoleResponseErrorType addUserListToRoleResponseErrorType) {
        return new AddUserToRoleResponseDTO(addUserListToRoleResponseErrorType, null);
    }

    public static AddUserToRoleResponseDTO success(RoleUserInfoDTO roleUserInfo) {
        return new AddUserToRoleResponseDTO(null, roleUserInfo);
    }

    public static GCSResponse<AddUserToRoleResponseDTO> responseError(AddUserListToRoleResponseErrorType addUserListToRoleResponseErrorType) {
        return GCSResponse.response(error(addUserListToRoleResponseErrorType));
    }

    public static GCSResponse<AddUserToRoleResponseDTO> responseSuccess(AddUserToRoleResponseDTO addUserToRoleResponseDTO) {
        return GCSResponse.response(success(addUserToRoleResponseDTO.getRoleUserInfo()));
    }

}


