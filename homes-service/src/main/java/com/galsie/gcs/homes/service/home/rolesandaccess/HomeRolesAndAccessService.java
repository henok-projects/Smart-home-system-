package com.galsie.gcs.homes.service.home.rolesandaccess;

import com.galsie.gcs.homes.configuration.security.contexthelper.HomesGalSecurityContextHelper;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.HomeStatus;
import com.galsie.gcs.homes.data.discrete.addusertorole.AddSingleUserToRoleResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addusertorole.AddUserListToRoleResponseErrorType;
import com.galsie.gcs.homes.data.discrete.deleterolefromhome.DeleteHomeRoleResponseErrorType;
import com.galsie.gcs.homes.data.discrete.deleteuserfromrole.RemoveSingleUserFromRoleResponseErrorType;
import com.galsie.gcs.homes.data.discrete.deleteuserfromrole.RemoveUserListFromRoleResponseErrorType;
import com.galsie.gcs.homes.data.discrete.editroleidentity.EditHomeRoleIdentityResponseErrorType;
import com.galsie.gcs.homes.data.discrete.editrolepermission.EditHomeRolePermissionsResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homesystemroletype.HomeSystemRoleType;
import com.galsie.gcs.homes.data.discrete.setcategoryhomerolepermissions.SetCategoryPermissionsResponseErrorType;
import com.galsie.gcs.homes.data.dto.roleandpermissions.addrole.request.AddHomeRoleRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.addrole.response.AddHomeRoleResponseDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.addusertorole.request.AddUserListToRoleRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.addusertorole.response.AddUserListToRoleResponseDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.common.RoleInfoDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.deleterolefromhome.request.DeleteHomeRoleRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.deleterolefromhome.response.DeleteHomeRoleResponseDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.editroleidentity.request.EditHomeRoleIdentityRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.editroleidentity.response.EditHomeRoleIdentityResponseDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.editrolepermissions.request.EditHomeRolePermissionRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.editrolepermissions.response.EditHomeRolePermissionsResponseDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.removeuserfromrole.request.RemoveUserListFromRoleRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.removeuserfromrole.response.RemoveUserListFromRoleResponseDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.setcategoryhomerolepermissions.request.SetSubCategoryHomeRolePermissionRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.setcategoryhomerolepermissions.response.SetCategoryHomeRolePermissionsResponseDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.setcategoryhomerolepermissions.response.SubCategoryPermissionInfoDTO;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleCategoryPermissionEntity;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleSubCategoryPermissionEntity;
import com.galsie.gcs.homes.repository.home.HomeEntityRepository;
import com.galsie.gcs.homes.repository.home.user.HomeUserAccessInfoRepository;
import com.galsie.gcs.homes.repository.home.user.HomeUserEntityRepository;
import com.galsie.gcs.homes.repository.homerole.HomeRoleCategoryPermissionRepository;
import com.galsie.gcs.homes.repository.homerole.HomeRoleEntityRepository;
import com.galsie.gcs.homes.repository.homerole.HomeRoleSubCategoryPermissionRepository;
import com.galsie.gcs.homes.service.home.HomeService;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserHelperService;
import com.galsie.gcs.homes.infrastructure.rolesandaccess.permissions.PermissionsCategory;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.galsie.gcs.homes.data.dto.roleandpermissions.addrole.response.AddHomeRoleResponseDTO.responseSuccess;
import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.response;
import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.saveEntityThrows;


@Service
public class HomeRolesAndAccessService {

    @Autowired
    HomeEntityRepository homeEntityRepository;

    @Autowired
    HomeRoleEntityRepository homeRoleEntityRepository;

    @Autowired
    HomeRoleCategoryPermissionRepository homeRoleCategoryPermissionRepository;

    @Autowired
    HomeRoleSubCategoryPermissionRepository homeRoleSubCategoryPermissionRepository;


    @Autowired
    HomeUserEntityRepository homeUserEntityRepository;

    @Autowired
    HomeUserAccessInfoRepository homeUserAccessInfoRepository;

    @Autowired
    HomesGalSecurityContextHelper homesGalSecurityContextHelper;

    @Autowired
    HomeRolesAndAccessCheckerService rolesAndPermissionsService;

    @Autowired
    HomeService homeService;
    @Autowired
    HomeUserHelperService homeUserHelperService;

    @Transactional
    public GCSResponse<AddHomeRoleResponseDTO> addHomeRole(AddHomeRoleRequestDTO addHomeRoleRequestDTO) throws GCSResponseException {
       // var userId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeId = addHomeRoleRequestDTO.getHomeId();
        var homeEntityOpt = homeEntityRepository.findById(homeId);

        //var homeUserEntity = homeUserService.gcsInternalFindByHomeIdAndUserId(homeId, userId);

        if (homeEntityOpt.isEmpty()) {
            return AddHomeRoleResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST,null);
        }

        if (homeEntityOpt.get().getStatus() !=HomeStatus.ACTIVE){
            return AddHomeRoleResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null);
        }

        // Check for permission
        // if (!rolesAndPermissionsService.gcsInternalDoesHomeUserHavePermissions(homeUserEntity.get().getUniqueId(), "home.management.roles.full")) {
        //  return AddHomeRoleResponseDTO.responseError(AddHomeRoleResponseErrorType.NO_PERMISSION);
        // }

        var newRole = HomeRoleEntity.builder()
                .name(addHomeRoleRequestDTO.getRoleName())
                .home(homeEntityOpt.get())
                .base64EncodedImage(addHomeRoleRequestDTO.getRoleIconImageReference())
                .build();
        saveEntityThrows(homeRoleEntityRepository, newRole).getResponseData();

        var categoryPermissionEntity = HomeRoleCategoryPermissionEntity.builder()
                .homeRole(newRole)
                .build();
        saveEntityThrows(homeRoleCategoryPermissionRepository, categoryPermissionEntity);

        var subcategoryPermissionEntity = HomeRoleSubCategoryPermissionEntity.builder()
                .categoryPermissionEntity(categoryPermissionEntity)
                .build();
        saveEntityThrows(homeRoleSubCategoryPermissionRepository, subcategoryPermissionEntity);

        var addHomeRoleResponseDTO = AddHomeRoleResponseDTO.builder()
                .roleInfo(RoleInfoDTO.fromHomeRoleEntity(newRole))
                .build();

        return responseSuccess(addHomeRoleResponseDTO);
    }

    @Transactional
    public GCSResponse<DeleteHomeRoleResponseDTO> deleteHomeRole(DeleteHomeRoleRequestDTO deleteHomeRoleRequestDTO) throws GCSResponseException {
        if(deleteHomeRoleRequestDTO.getHomeId() == null) {
            return DeleteHomeRoleResponseDTO.responseError(HomeResponseErrorType.INVALID_HOME_ID, null);
        }
       // var userId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeId = deleteHomeRoleRequestDTO.getHomeId();

       // var homeUserEntity = homeUserService.gcsInternalFindByHomeIdAndUserId(homeId, userId);
        var homeEntityOpt = homeEntityRepository.findById(homeId);
        if (homeEntityOpt.isEmpty()) {
            return DeleteHomeRoleResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST, null);
        }
        var home = homeEntityOpt.get();
        if (home.getStatus() !=HomeStatus.ACTIVE){
            return DeleteHomeRoleResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null);
        }

        // Check for permission
//        if (!rolesAndPermissionsService.gcsInternalDoesHomeUserHavePermissions(homeUserEntity.get().getUniqueId(), "home.management.delete.full")) {
//            return DeleteHomeRoleResponseDTO.responseError(DeleteHomeRoleResponseErrorType.NO_PERMISSION);
//        }


        var role = homeRoleEntityRepository.findById(deleteHomeRoleRequestDTO.getRoleId());
        if (role.isEmpty()) {
            return DeleteHomeRoleResponseDTO.responseError(null, DeleteHomeRoleResponseErrorType.ROLE_NOT_FOUND);
        }

        if (Objects.equals((HomeSystemRoleType.HOUSEMASTER).toString(), role.get().getName())) {
            return DeleteHomeRoleResponseDTO.responseError(null, DeleteHomeRoleResponseErrorType.SYSTEM_ROLE_CANT_BE_DELETED);
        }

        homeRoleEntityRepository.delete(role.get());

        return DeleteHomeRoleResponseDTO.responseSuccess();
    }


    @Transactional
    public GCSResponse<EditHomeRolePermissionsResponseDTO> editRolePermissions(EditHomeRolePermissionRequestDTO editHomeRolePermissionRequestDTO) {
       // var userId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeId = editHomeRolePermissionRequestDTO.getHomeId();

        var homeEntityOpt = homeEntityRepository.findById(editHomeRolePermissionRequestDTO.getHomeId());


        if (homeEntityOpt.isEmpty()) {
            return EditHomeRolePermissionsResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST, null);
        }


        if (homeEntityOpt.get().getStatus() != HomeStatus.ACTIVE){
            return EditHomeRolePermissionsResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null);
        }

        if (editHomeRolePermissionRequestDTO.getRoleId() == null ) {
            return EditHomeRolePermissionsResponseDTO.responseError(null, EditHomeRolePermissionsResponseErrorType.INVALID_ROLE_ID);
        }

        var existingRoleOpt = homeRoleEntityRepository.findById(editHomeRolePermissionRequestDTO.getRoleId());
        if (existingRoleOpt.isEmpty()) {
            return EditHomeRolePermissionsResponseDTO.responseError(null, EditHomeRolePermissionsResponseErrorType.ROLE_NOT_FOUND);
        }

        var existingRole = homeRoleEntityRepository.findById(editHomeRolePermissionRequestDTO.getRoleId()).get();

        if (Objects.equals((HomeSystemRoleType.HOUSEMASTER).toString(), existingRole.getName())) {
            return EditHomeRolePermissionsResponseDTO.responseError(null, EditHomeRolePermissionsResponseErrorType.HOUSEMASTER_PERMISSIONS_CANT_BE_EDITED);
        }

        // Assuming each role has only one category permission and each category permission has one subcategory permission
        if (!existingRole.getCategories().isEmpty()) {
            var categoryPermission = existingRole.getCategories().get(0);

            if (!categoryPermission.getSubCategoryPermissionEntities().isEmpty()) {
                var subCategoryPermission = categoryPermission.getSubCategoryPermissionEntities().get(0);

                // Update the existing subCategoryPermission with new permissions
                subCategoryPermission.setWithPermissions(editHomeRolePermissionRequestDTO.getWithPermissions());
                subCategoryPermission.setWithoutPermissions(editHomeRolePermissionRequestDTO.getWithoutPermissions());

                // Save the updated subCategoryPermission
                homeRoleSubCategoryPermissionRepository.save(subCategoryPermission);
            }
        }

        // Update and save the existingRole if needed (e.g., if other role properties are being changed)

        var roleInfo = RoleInfoDTO.fromHomeRoleEntity(existingRole);
        var responseDTO = EditHomeRolePermissionsResponseDTO.builder()
                .roleInfo(roleInfo)
                .build();

        return EditHomeRolePermissionsResponseDTO.responseSuccess(responseDTO);
    }


    @Transactional
    public GCSResponse<SetCategoryHomeRolePermissionsResponseDTO> setCategoryPermissions(SetSubCategoryHomeRolePermissionRequestDTO setSubCategoryHomeRolePermissionRequestDTO) {

        // var userId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeId = setSubCategoryHomeRolePermissionRequestDTO.getHomeId();
        // var homeUserEntity = homeUserService.gcsInternalFindByHomeIdAndUserId(homeId, userId);

        var homeEntityOpt = homeEntityRepository.findById(setSubCategoryHomeRolePermissionRequestDTO.getHomeId());
        if (homeEntityOpt.isEmpty()) {
            return SetCategoryHomeRolePermissionsResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST, null);
        }

        if (homeEntityOpt.get().getStatus() != HomeStatus.ACTIVE){
            return SetCategoryHomeRolePermissionsResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null);
        }

        if(setSubCategoryHomeRolePermissionRequestDTO.getRoleId() == null) {
            return SetCategoryHomeRolePermissionsResponseDTO.responseError(null, SetCategoryPermissionsResponseErrorType.INVALID_ROLE_ID);
        }

        var existingRole = homeRoleEntityRepository.findById(setSubCategoryHomeRolePermissionRequestDTO.getRoleId());
        if(existingRole.isEmpty()) {
            return SetCategoryHomeRolePermissionsResponseDTO.responseError(null, SetCategoryPermissionsResponseErrorType.INVALID_ROLE_ID);
        }
        // Check permissions here using your permission validation logic before continuing
//        if (!rolesAndPermissionsService.gcsInternalDoesHomeUserHavePermissions(homeUserEntity.get().getUniqueId(), "home.management.delete.full")) {
//             return SetCategoryHomeRolePermissionsResponseDTO.responseError(SetCategoryPermissionsResponseErrorType.NO_PERMISSION);
//        }

        var permissionsCategory = PermissionsCategory.valueOf(setSubCategoryHomeRolePermissionRequestDTO.getCategoryKey().toUpperCase());
        if (permissionsCategory.getKey() == null) {
            return SetCategoryHomeRolePermissionsResponseDTO.responseError(null, SetCategoryPermissionsResponseErrorType.INVALID_CATEGORY_KEY);
        }

        if (setSubCategoryHomeRolePermissionRequestDTO.getSubCategoryKey() == null) {
            return SetCategoryHomeRolePermissionsResponseDTO.responseError(null, SetCategoryPermissionsResponseErrorType.INVALID_SUBCATEGORY_KEY);
        }

        HomeRoleCategoryPermissionEntity categoryPermissionEntity = new HomeRoleCategoryPermissionEntity();
        categoryPermissionEntity.setHomeRole(existingRole.get());
        categoryPermissionEntity.setCategoryKey(setSubCategoryHomeRolePermissionRequestDTO.getCategoryKey());

        categoryPermissionEntity = homeRoleCategoryPermissionRepository.save(categoryPermissionEntity);

        for (String permission : setSubCategoryHomeRolePermissionRequestDTO.getWithPermissions()) {
            HomeRoleSubCategoryPermissionEntity subCategoryPermission = new HomeRoleSubCategoryPermissionEntity();
            subCategoryPermission.setCategoryPermissionEntity(categoryPermissionEntity); // Link to the parent category
            homeRoleSubCategoryPermissionRepository.save(subCategoryPermission);
        }

        SubCategoryPermissionInfoDTO subCategoryPermissionInfoDTO = SubCategoryPermissionInfoDTO.builder()
                .homeId(homeEntityOpt.get().getHomeId())
                .roleId(existingRole.get().getId())
                .subCategoryKey(setSubCategoryHomeRolePermissionRequestDTO.getCategoryKey())
                .subCategoryKey(setSubCategoryHomeRolePermissionRequestDTO.getSubCategoryKey())
                .withPermissions(setSubCategoryHomeRolePermissionRequestDTO.getWithPermissions())
                .withoutPermissions(setSubCategoryHomeRolePermissionRequestDTO.getWithoutPermissions())
                .build();

        var responseDTO = SetCategoryHomeRolePermissionsResponseDTO.builder()
                .subcategoryPermissionInfo(subCategoryPermissionInfoDTO)
                .build();

        return SetCategoryHomeRolePermissionsResponseDTO.responseSuccess(responseDTO);
    }


    @Transactional
    public GCSResponse<EditHomeRoleIdentityResponseDTO> editRoleIdentity(EditHomeRoleIdentityRequestDTO editHomeRoleIdentityRequestDTO) throws GCSResponseException {

       // var userSecurityAuthSession = homesGalSecurityContextHelper.getUserAuthSession();
        //var userId = userSecurityAuthSession.getUserId();
        var homeId = editHomeRoleIdentityRequestDTO.getHomeId();

      //  var homeUserEntity = homeUserService.gcsInternalFindByHomeIdAndUserId(homeId, userId);
        var homeEntityOpt = homeEntityRepository.findById(homeId);

        if (homeEntityOpt.isEmpty()) {
            return EditHomeRoleIdentityResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST, null);
        }

        if (homeEntityOpt.get().getStatus() != HomeStatus.ACTIVE){
            return EditHomeRoleIdentityResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null);
        }

        if (editHomeRoleIdentityRequestDTO.getRoleId() == null) {
            return EditHomeRoleIdentityResponseDTO.responseError(null, EditHomeRoleIdentityResponseErrorType.INVALID_ROLE_ID);
        }

        var existingRole = homeRoleEntityRepository.findById(editHomeRoleIdentityRequestDTO.getRoleId());
        if (existingRole.isEmpty()) {
            return EditHomeRoleIdentityResponseDTO.responseError(null, EditHomeRoleIdentityResponseErrorType.ROLE_NOT_FOUND);
        }

        var role = existingRole.get();
        // Check if role is a system role by checking if the systemRoleType attribute matches HOUSEMASTER
        if (Objects.equals((HomeSystemRoleType.HOUSEMASTER).toString(), existingRole.get().getName())) {
            return EditHomeRoleIdentityResponseDTO.responseError(null, EditHomeRoleIdentityResponseErrorType.CANT_EDIT_SYSTEM_ROLES_IDENTITY);
        }

        role.setName(editHomeRoleIdentityRequestDTO.getRoleName());
        role.setBase64EncodedImage(editHomeRoleIdentityRequestDTO.getRoleIconImageReference());
        saveEntityThrows(homeRoleEntityRepository, role).getResponseData();

        return EditHomeRoleIdentityResponseDTO.responseSuccess();
    }

    @Transactional
    public GCSResponse<AddUserListToRoleResponseDTO> addUserToRole(AddUserListToRoleRequestDTO addUserListToRoleRequestDTO) throws GCSResponseException {

       // var usersId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeId = addUserListToRoleRequestDTO.getHomeId();

        var homeEntity = homeEntityRepository.findById(homeId);
        if (!homeExists(addUserListToRoleRequestDTO.getHomeId())) {
            return AddUserListToRoleResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST, null);
        }

        if (homeEntity.get().getStatus() != HomeStatus.ACTIVE) {
            return AddUserListToRoleResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null);
        }

        if (!roleExists(addUserListToRoleRequestDTO.getRoleId())) {
            return AddUserListToRoleResponseDTO.responseError(null, AddUserListToRoleResponseErrorType.ROLE_DOESNT_EXIST);
        }

        var roleEntity = homeRoleEntityRepository.findById(addUserListToRoleRequestDTO.getRoleId());

        List<AddSingleUserToRoleResponseErrorType> errors = new ArrayList<>();
        for (Long userId : addUserListToRoleRequestDTO.getHomeUserIds()) {
            var userEntity = homeUserEntityRepository.findById(userId);
            if (!havePermissionsAddUserToRole(userId, addUserListToRoleRequestDTO.getRoleId())) {
                errors.add(AddSingleUserToRoleResponseErrorType.CANT_ADD_A_USER_TO_A_ROLE_WITH_MORE_PERMISSIONS_THAN_YOU);
                continue;
            }

            if (!userPartOfRole(userId, addUserListToRoleRequestDTO.getRoleId())) {
                errors.add(AddSingleUserToRoleResponseErrorType.USER_IS_NOT_PART_OF_THIS_HOME);
                continue;
            }

            var accessInfoEntity = userEntity.get().getHomeUserAccessInfoEntity();
            accessInfoEntity.getRoles().add(roleEntity.get());
            saveEntityThrows(homeUserAccessInfoRepository, accessInfoEntity);
            errors.add(null);
        }

        var res = AddUserListToRoleResponseDTO.builder()
                .addSingleUserToRoleErrors(errors)
                .build();

        return response(res);
    }

    @Transactional
    public GCSResponse<RemoveUserListFromRoleResponseDTO> removeUsersFromRole(RemoveUserListFromRoleRequestDTO removeUserListFromRoleRequestDTO) throws GCSResponseException {

        var roleEntity = homeRoleEntityRepository.findById(removeUserListFromRoleRequestDTO.getRoleId());
        var homeEntity = homeEntityRepository.findById(removeUserListFromRoleRequestDTO.getHomeId());
        if (roleEntity.isEmpty()) {
            return RemoveUserListFromRoleResponseDTO.responseError(null, RemoveUserListFromRoleResponseErrorType.ROLE_DOESNT_EXIST);
        }

        if (homeEntity.isEmpty()) {
            return RemoveUserListFromRoleResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST, null);
        }

        if (homeEntity.get().getStatus() != HomeStatus.ACTIVE) {
            return RemoveUserListFromRoleResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null);
        }

        List<RemoveSingleUserFromRoleResponseErrorType> errorList = new ArrayList<>();

        for (Long userIdToRemove : removeUserListFromRoleRequestDTO.getHomeUserIds()) {
            var userEntity = homeUserEntityRepository.findById(userIdToRemove);
            if (!userPartOfRole(userIdToRemove, removeUserListFromRoleRequestDTO.getRoleId())) {
                errorList.add(RemoveSingleUserFromRoleResponseErrorType.USER_IS_NOT_PART_OF_THIS_ROLE);
                continue;
            }

            var accessInfoEntity = userEntity.get().getHomeUserAccessInfoEntity();
            accessInfoEntity.getRoles().remove(roleEntity.get());
            homeUserAccessInfoRepository.save(accessInfoEntity);

            errorList.add(null);
        }

        return RemoveUserListFromRoleResponseDTO.responseSuccess(errorList);
    }

    private boolean homeExists(Long homeId) {
        var homeEntityOpt = homeEntityRepository.findById(homeId);
        return homeEntityOpt.isPresent();
    }

    private boolean roleExists(Long roleId) {
        Optional<HomeRoleEntity> role = homeRoleEntityRepository.findById(roleId);
        return role.isPresent();
    }

    private boolean havePermissionsAddUserToRole(Long userId, Long roleId) {
        if (!homeRoleEntityRepository.existsById(roleId)) {
            return false; // Role does not exist
        }

        // Check if the user exists
        if (!homeRoleEntityRepository.existsById(userId)) {
            return false; // User does not exist
        }

        // Check permissions here using your permission validation logic
        return rolesAndPermissionsService.gcsInternalDoesHomeUserHavePermissions(userId, Collections.singletonList("home.management.delete.full"));
    }

    private boolean userPartOfRole(Long userId, Long homeId) {
        return !homeRoleEntityRepository.findByUserIdAndHomeId(userId, homeId).isEmpty();
    }

    @Transactional
    private void removeUserFromRole(Long userId, Long roleId) {
        List<HomeRoleEntity> userRoles = homeRoleEntityRepository.findByUserIdAndHomeId(userId, roleId);
        if (!userRoles.isEmpty()) {
            for (HomeRoleEntity userRole : userRoles) {
                homeRoleEntityRepository.delete(userRole);
            }
        } else {
            throw new IllegalStateException("User is not assigned to any role with the provided ID and cannot be removed.");
        }
    }

}

