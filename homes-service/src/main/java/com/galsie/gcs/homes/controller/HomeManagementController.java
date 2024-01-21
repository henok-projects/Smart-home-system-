package com.galsie.gcs.homes.controller;

import com.galsie.gcs.homes.data.dto.addarea.request.AddHomeAreaRequestDTO;
import com.galsie.gcs.homes.data.dto.adddoor.request.AddAreaDoorRequestDTO;
import com.galsie.gcs.homes.data.dto.addwindow.request.AddAreaWindowRequestDTO;
import com.galsie.gcs.homes.data.dto.deletehome.request.DeleteHomeRequestDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.deleteuser.request.DeleteHomeUsersRequestDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.editaccess.request.EditHomeUsersAccessInfoRequestDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.endaccess.request.EndHomeUsersAccessRequestDTO;
import com.galsie.gcs.homes.data.dto.invites.request.qr.QRCodeInviteRequestDTO;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserSetInviteRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.addrole.request.AddHomeRoleRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.addusertorole.request.AddUserListToRoleRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.deleterolefromhome.request.DeleteHomeRoleRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.editroleidentity.request.EditHomeRoleIdentityRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.editrolepermissions.request.EditHomeRolePermissionRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.removeuserfromrole.request.RemoveUserListFromRoleRequestDTO;
import com.galsie.gcs.homes.data.dto.roleandpermissions.setcategoryhomerolepermissions.request.SetSubCategoryHomeRolePermissionRequestDTO;
import com.galsie.gcs.homes.service.home.DeleteHomeService;
import com.galsie.gcs.homes.service.home.area.HomeAreaManagementService;
import com.galsie.gcs.homes.service.home.invites.HomeUserInviteService;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserManagementService;
import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeManagementController {


    @Autowired
    DeleteHomeService deleteHomeService;

    @Autowired
    HomeUserManagementService homeUserManagementService;

    @Autowired
    HomeAreaManagementService homeAreaManagementService;

    @Autowired
    HomeUserInviteService homeUserInviteService;

    @Autowired
    HomeRolesAndAccessService homeRolesAndAccessService;


    @PostMapping("/management/delete")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<? extends GCSResponseDTO<?>> deleteHome(@RequestBody DeleteHomeRequestDTO deleteHomeRequestDto) {
        return deleteHomeService.deleteHome(deleteHomeRequestDto).toResponseEntity();
    }

    @PostMapping("/management/area/add")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<? extends GCSResponseDTO<?>> addHomeArea(@RequestBody AddHomeAreaRequestDTO addAreaRequestDTO) {
        return homeAreaManagementService.addHomeArea(addAreaRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/area/addDoor")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> addHomerAreaDoor(@RequestBody AddAreaDoorRequestDTO addAreaDoorRequestDTO) {
        return homeAreaManagementService.addHomeAreaDoor(addAreaDoorRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/area/addWindow")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> addHomerAreaWindow(@RequestBody AddAreaWindowRequestDTO addAreaWindowRequestDTO) {
        return homeAreaManagementService.addHomeAreaWindow(addAreaWindowRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/invites/createUserInvite")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> inviteUser(@RequestBody HomeDirectUserSetInviteRequestDTO homeDirectUserSetInviteRequestDTO) {
        return homeUserInviteService.requestPerformHomeUserInvite(homeDirectUserSetInviteRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/invites/createQRUserInvite")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> qrInvite(@RequestBody QRCodeInviteRequestDTO qrCodeInviteRequestDTO) {
        return homeUserInviteService.requestCreateHomeQRInvite(qrCodeInviteRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/role/addRole")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> addRole(@RequestBody AddHomeRoleRequestDTO addHomeRoleRequestDTO) {
        return homeRolesAndAccessService.addHomeRole(addHomeRoleRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/role/deleteRole")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> removeRole(@RequestBody DeleteHomeRoleRequestDTO deleteHomeRoleRequestDTO) {
        return homeRolesAndAccessService.deleteHomeRole(deleteHomeRoleRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/role/editPermission")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editRolePermissions(@RequestBody EditHomeRolePermissionRequestDTO editHomeRolePermissionRequestDTO) {
        return homeRolesAndAccessService.editRolePermissions(editHomeRolePermissionRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/role/setCategoryPermission")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> setCategoryPermissions(@RequestBody SetSubCategoryHomeRolePermissionRequestDTO setSubCategoryHomeRolePermissionRequestDTO) {
        return homeRolesAndAccessService.setCategoryPermissions(setSubCategoryHomeRolePermissionRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/role/editRoleIdentity")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editRoleIdentity(@RequestBody EditHomeRoleIdentityRequestDTO editHomeRoleIdentityRequestDTO) {
        return homeRolesAndAccessService.editRoleIdentity(editHomeRoleIdentityRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/role/addUser")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> addUserToRole(@RequestBody AddUserListToRoleRequestDTO addUserListToRoleRequestDTO) {
        return homeRolesAndAccessService.addUserToRole(addUserListToRoleRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/role/removeUsers")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> removeUserFromRole(@RequestBody RemoveUserListFromRoleRequestDTO removeUserListFromRoleRequestDTO) {
        return homeRolesAndAccessService.removeUsersFromRole(removeUserListFromRoleRequestDTO).toResponseEntity();
    }
    @PostMapping("/management/users/editUsersAccess")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> editUserAccess(@RequestBody EditHomeUsersAccessInfoRequestDTO editHomeUsersAccessInfoRequestDTO) {
        return homeUserManagementService.editUserAccess(editHomeUsersAccessInfoRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/users/endUsersAccess")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> endUsersAccess(@RequestBody EndHomeUsersAccessRequestDTO endHomeUsersAccessRequestDTO) {
        return homeUserManagementService.endUserAccess(endHomeUsersAccessRequestDTO).toResponseEntity();
    }

    @PostMapping("/management/users/deleteUsers")
    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    public ResponseEntity<?> deleteHomeUsers(@RequestBody DeleteHomeUsersRequestDTO deleteHomeUsersRequestDTO) {
        return homeUserManagementService.deleteHomeUsers(deleteHomeUsersRequestDTO).toResponseEntity();

    }
}
