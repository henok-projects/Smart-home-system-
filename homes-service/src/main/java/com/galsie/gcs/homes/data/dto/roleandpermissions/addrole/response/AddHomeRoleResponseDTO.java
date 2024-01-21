package com.galsie.gcs.homes.data.dto.roleandpermissions.addrole.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addrole.AddHomeRoleResponseErrorType;
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
public class AddHomeRoleResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("add_role_response_error")
    @Nullable
    private AddHomeRoleResponseErrorType addRoleResponseError;

    @JsonProperty("role_info")
    @Nullable
    private RoleInfoDTO roleInfo;


    public static AddHomeRoleResponseDTO error(HomeResponseErrorType homeResponseError, AddHomeRoleResponseErrorType addRoleResponseError) {
        return new AddHomeRoleResponseDTO(homeResponseError, addRoleResponseError, null);
    }

    public static AddHomeRoleResponseDTO success(RoleInfoDTO roleInfo) {
        return new AddHomeRoleResponseDTO(null, null, roleInfo);
    }

    public static GCSResponse<AddHomeRoleResponseDTO> responseError(HomeResponseErrorType homeResponseError, AddHomeRoleResponseErrorType addRoleResponseError) {
        return GCSResponse.response(error(homeResponseError, addRoleResponseError));
    }

    public static GCSResponse<AddHomeRoleResponseDTO> responseSuccess(AddHomeRoleResponseDTO addHomeRoleResponseDTO) {
        return GCSResponse.response(success(addHomeRoleResponseDTO.getRoleInfo()));
    }
}
