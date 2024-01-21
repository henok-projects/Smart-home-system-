package com.galsie.gcs.homes.data.dto.roleandpermissions.setcategoryhomerolepermissions.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.setcategoryhomerolepermissions.SetCategoryPermissionsResponseErrorType;
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
public class SetCategoryHomeRolePermissionsResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("set_category_permissions_response_error")
    @Nullable
    private SetCategoryPermissionsResponseErrorType setCategoryPermissionsResponseError;

    @JsonProperty("permission_info")
    @Nullable
    private SubCategoryPermissionInfoDTO subcategoryPermissionInfo;



    public static SetCategoryHomeRolePermissionsResponseDTO error(HomeResponseErrorType homeResponseError, SetCategoryPermissionsResponseErrorType setCategoryPermissionsResponseError) {
        return new SetCategoryHomeRolePermissionsResponseDTO(homeResponseError, setCategoryPermissionsResponseError, null);
    }

    public static SetCategoryHomeRolePermissionsResponseDTO success(SubCategoryPermissionInfoDTO permissionInfo) {
        return new SetCategoryHomeRolePermissionsResponseDTO(null, null, permissionInfo);
    }

    public static GCSResponse<SetCategoryHomeRolePermissionsResponseDTO> responseError(HomeResponseErrorType homeResponseError, SetCategoryPermissionsResponseErrorType setCategoryPermissionsResponseError) {
        return GCSResponse.response(error(homeResponseError, setCategoryPermissionsResponseError));
    }

    public static GCSResponse<SetCategoryHomeRolePermissionsResponseDTO> responseSuccess(SetCategoryHomeRolePermissionsResponseDTO setCategoryHomeRolePermissionsResponseDTO) {
        return GCSResponse.response(success(setCategoryHomeRolePermissionsResponseDTO.getSubcategoryPermissionInfo()));
    }
}
