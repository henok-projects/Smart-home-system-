package com.galsie.gcs.homes.service.home.homeusers;

import com.galsie.gcs.homes.configuration.security.contexthelper.HomesGalSecurityContextHelper;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.HomeStatus;
import com.galsie.gcs.homes.data.discrete.HomeUserStatus;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.deleteuser.DeleteHomeUsersResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.deleteuser.DeleteSingleHomeUserResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.editaccess.EditHomeUsersAccessResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.editaccess.EditSingleHomeUserAccessResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.endaccess.EndHomeUsersAccessResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.endaccess.EndSingleUserHomeAccessResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.deleteuser.request.DeleteHomeUsersRequestDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.deleteuser.response.DeleteHomeUsersResponseDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.deleteuser.response.DeleteSingleHomeUserResponseDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.editaccess.request.EditHomeUsersAccessInfoRequestDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.editaccess.response.EditHomeUsersAccessResponseDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.editaccess.response.EditSingleHomeUserAccessResponseDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.endaccess.request.EndHomeUsersAccessRequestDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.endaccess.response.EndHomeUsersAccessResponseDTO;
import com.galsie.gcs.homes.data.dto.homeuseraccess.endaccess.response.EndSingleHomeUserAccessResponseDTO;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserAccessInfoEntity;
import com.galsie.gcs.homes.repository.home.HomeEntityRepository;
import com.galsie.gcs.homes.repository.home.user.HomeUserAccessInfoRepository;
import com.galsie.gcs.homes.repository.home.user.HomeUserEntityRepository;
import com.galsie.gcs.homes.repository.homerole.HomeRoleEntityRepository;
import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessCheckerService;
import com.galsie.gcs.homescommondata.repository.home.user.AbstractHomeUserEntityRepository;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.response;
import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.saveEntityThrows;

/**
 * Manages home users
 * - User Access
 * - Deleting users
 * - ...
 */
@Service
public class HomeUserManagementService {

    @Autowired
    HomeEntityRepository homeEntityRepository;
    @Autowired
    HomeUserEntityRepository homeUserRepository;
    @Autowired
    HomeRoleEntityRepository homeRoleEntityRepository;
    @Autowired
    AbstractHomeUserEntityRepository abstractHomeUserEntityRepository;
    @Autowired
    HomeUserAccessInfoRepository homeUserAccessInfoRepository;
    @Autowired
    HomesGalSecurityContextHelper homesGalSecurityContextHelper;
    @Autowired
    HomeRolesAndAccessCheckerService rolesAndPermissionsService;


    public GCSResponse<EditHomeUsersAccessResponseDTO> editUserAccess(EditHomeUsersAccessInfoRequestDTO editHomeUsersAccessInfoRequestDTO) {
        try {
            return gcsInternalEditUserAccess(editHomeUsersAccessInfoRequestDTO);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(EditHomeUsersAccessResponseDTO.class);
        }
    }

    @Transactional
    GCSResponse<EditHomeUsersAccessResponseDTO> gcsInternalEditUserAccess(EditHomeUsersAccessInfoRequestDTO editHomeUsersAccessInfoRequestDTO) throws GCSResponseException {
        var userId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeId = editHomeUsersAccessInfoRequestDTO.getHomeId();
        var homeEntityOpt = homeEntityRepository.findById(homeId);

        if (homeEntityOpt.isEmpty()) {
            return EditHomeUsersAccessResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST, null);
        }

        if (homeEntityOpt.get().getStatus() != HomeStatus.ACTIVE) {
            return EditHomeUsersAccessResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null);
        }

        if (!rolesAndPermissionsService.gcsInternalDoesHomeUserHavePermissions(userId, editHomeUsersAccessInfoRequestDTO.getAccessInfo().getPermissions())) {
            return EditHomeUsersAccessResponseDTO.responseError(HomeResponseErrorType.NO_PERMISSION, null);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        List<EditSingleHomeUserAccessResponseDTO> editResponses = new ArrayList<>();

        for (Long uId : editHomeUsersAccessInfoRequestDTO.getHomeUserIds()) {
            //TODO  Check for permission to delete this specific user
            if (uId.toString().isEmpty()) {
                return EditHomeUsersAccessResponseDTO.responseSuccess(List.of(EditSingleHomeUserAccessResponseDTO.error( EditSingleHomeUserAccessResponseErrorType.INVALID_USER_ID)));
            }

            // Check if current user is part of this home
            var currentUserHomeEntityOpt = abstractHomeUserEntityRepository.findByAppUserAppUserIdAndHomeHomeId(uId, homeId);
            if (currentUserHomeEntityOpt.isEmpty()) {
                return EditHomeUsersAccessResponseDTO.responseError(HomeResponseErrorType.NOT_PART_OF_THIS_HOME, null);
            }

            var userOpt = homeUserRepository.findById(uId);

            if (userOpt.isPresent()) {
                var accessInfo = editHomeUsersAccessInfoRequestDTO.getAccessInfo();


                LocalDate startDate = null;
                LocalDate endDate = null;
                var singleResponse = new EditSingleHomeUserAccessResponseDTO();

                if (accessInfo.getAccessStartDate() != null) {
                    startDate = LocalDate.parse(accessInfo.getAccessStartDate(), formatter);
                }

                if (accessInfo.getAccessEndDate() != null) {
                    endDate = LocalDate.parse(accessInfo.getAccessEndDate(), formatter);
                }

                if (startDate != null && endDate != null) {
                    if (startDate.isAfter(endDate)) {
                        return EditHomeUsersAccessResponseDTO.responseError(null, EditHomeUsersAccessResponseErrorType.START_DATES_MUST_PRECEDE_END_DATES);
                    }
                }
                var homeUserAccessInfo = userOpt.get().getHomeUserAccessInfoEntity();
                if (homeUserAccessInfo.getHomeUserStatus() != HomeUserStatus.ACTIVE) {
                    return EditHomeUsersAccessResponseDTO.responseSuccess(List.of(EditSingleHomeUserAccessResponseDTO.error( EditSingleHomeUserAccessResponseErrorType.USER_NOT_PART_OF_THIS_HOME)));
                }

                if (startDate != null) {
                    homeUserAccessInfo.setAccessStartDate(startDate.atStartOfDay());
                }
                if (endDate != null) {
                    if (endDate.isBefore(ChronoLocalDate.from(homeUserAccessInfo.getAccessEndDate())) || endDate.isEqual(ChronoLocalDate.from(homeUserAccessInfo.getAccessEndDate()))) {
                        homeUserAccessInfo.setAccessEndDate(endDate.atTime(LocalTime.MAX));
                    } else {
                        return EditHomeUsersAccessResponseDTO.responseError(null, EditHomeUsersAccessResponseErrorType.INVALID_DATES);
                    }
                }

                if(accessInfo.getPermissions() != null) {
                    homeUserAccessInfo.setWithPermissions(accessInfo.getPermissions());
                }

                if (accessInfo.getRoles() != null) {
                    List<HomeRoleEntity> roles = accessInfo.getRoles().stream()
                            .map(roleId -> homeRoleEntityRepository.findById(roleId).orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    homeUserAccessInfo.setRoles(roles);
                }



                saveEntityThrows(homeUserAccessInfoRepository, homeUserAccessInfo);

                singleResponse.setHomeUserId(uId);
                singleResponse.setAccessInfo(accessInfo);


                editResponses.add(singleResponse);
            } else {
                return EditHomeUsersAccessResponseDTO.responseSuccess(List.of(EditSingleHomeUserAccessResponseDTO.error( EditSingleHomeUserAccessResponseErrorType.USER_DOESNT_EXIST)));
            }
        }

        var response = EditHomeUsersAccessResponseDTO.builder()
                .editSingleHomeUserAccessResponseDTOList(editResponses)
                .build();

        return response(response);
    }

    public GCSResponse<?> endUserAccess(EndHomeUsersAccessRequestDTO endHomeUsersAccessRequestDTO) {
        try {
            return gcsInternalEndUserAccess(endHomeUsersAccessRequestDTO);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(EndHomeUsersAccessResponseDTO.class);
        }
    }

    @Transactional
    GCSResponse<?> gcsInternalEndUserAccess(EndHomeUsersAccessRequestDTO endHomeUsersAccessRequestDTO) throws GCSResponseException {
        var userId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeId = endHomeUsersAccessRequestDTO.getHomeId();
        var homeEntityOpt = homeEntityRepository.findById(homeId);

        if (homeEntityOpt.isEmpty()) {
            return EndHomeUsersAccessResponseDTO.responseError(EndHomeUsersAccessResponseErrorType.HOME_DOESNT_EXIST);
        }

        if (homeEntityOpt.get().getStatus() != HomeStatus.ACTIVE) {
            return EndHomeUsersAccessResponseDTO.responseError(EndHomeUsersAccessResponseErrorType.HOME_ISNT_ACTIVE);
        }

        if (!rolesAndPermissionsService.gcsInternalDoesHomeUserHavePermissions(userId, Collections.singletonList("home.management.roles.full"))) {
            return EndHomeUsersAccessResponseDTO.responseError(EndHomeUsersAccessResponseErrorType.NO_PERMISSION);
        }

        List<EndSingleHomeUserAccessResponseDTO> endResponses = new ArrayList<>();

        for (Long uId : endHomeUsersAccessRequestDTO.getHomeUserIds()) {
            //TODO  Check for permission(s) to delete this specific user
            if (uId.toString().isEmpty()) {
                return EndSingleHomeUserAccessResponseDTO.responseError(EndSingleUserHomeAccessResponseErrorType.INVALID_USER_ID);
            }

            // Check if current user is part of this home
            var currentUserHomeEntityOpt = abstractHomeUserEntityRepository.findByAppUserAppUserIdAndHomeHomeId(uId, homeId);
            if (currentUserHomeEntityOpt.isEmpty()) {
                return EndHomeUsersAccessResponseDTO.responseError(EndHomeUsersAccessResponseErrorType.NOT_PART_OF_THIS_HOME);
            }

            var userOpt = homeUserRepository.findById(uId);
            if (userOpt.isPresent()) {
                var homeUserAccessInfo = userOpt.get().getHomeUserAccessInfoEntity();
                if (homeUserAccessInfo.getHomeUserStatus() != HomeUserStatus.ACTIVE) {
                    return EndSingleHomeUserAccessResponseDTO.responseError(EndSingleUserHomeAccessResponseErrorType.USER_NOT_PART_OF_THIS_HOME);
                }

                var singleResponse = new EndSingleHomeUserAccessResponseDTO();
                singleResponse.setHomeUserId(uId);

                boolean accessEnded = endAccessForUser(homeUserAccessInfo);
                if (accessEnded) {
                    singleResponse.setAccessInfo(UserHomeAccessInfoDTO.fromUserAccessInfoEntity(homeUserAccessInfo));
                } else {
                    return EndSingleHomeUserAccessResponseDTO.responseError(EndSingleUserHomeAccessResponseErrorType.ACCESS_ALREADY_ENDED);
                }

                endResponses.add(singleResponse);
            } else {
                return EndSingleHomeUserAccessResponseDTO.responseError(EndSingleUserHomeAccessResponseErrorType.USER_DOESNT_EXIST);
            }
        }

        var response = EndHomeUsersAccessResponseDTO.builder()
                .endHomeUserAccessResponses(endResponses)
                .build();

        return response(response);
    }


    public GCSResponse<?> deleteHomeUsers(DeleteHomeUsersRequestDTO deleteHomeUsersRequestDTO) {
        try {
            return gcsInternalDeleteHomeUsers(deleteHomeUsersRequestDTO);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(DeleteHomeUsersResponseDTO.class);
        }
    }

    @Transactional
    public GCSResponse<?> gcsInternalDeleteHomeUsers(DeleteHomeUsersRequestDTO deleteHomeUsersRequestDTO) throws GCSResponseException {
        var currentUserId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeId = deleteHomeUsersRequestDTO.getHomeId();
        var homeEntityOpt = homeEntityRepository.findById(homeId);

        if (homeEntityOpt.isEmpty()) {
            return DeleteHomeUsersResponseDTO.responseError(DeleteHomeUsersResponseErrorType.HOME_DOESNT_EXIST);
        }

        if (homeEntityOpt.get().getStatus() != HomeStatus.ACTIVE) {
            return DeleteHomeUsersResponseDTO.responseError(DeleteHomeUsersResponseErrorType.HOME_ISNT_ACTIVE);
        }

        if (!rolesAndPermissionsService.gcsInternalDoesHomeUserHavePermissions(currentUserId, Collections.singletonList("home.management.roles.full"))) {
            return DeleteHomeUsersResponseDTO.responseError(DeleteHomeUsersResponseErrorType.NO_PERMISSION);
        }

        List<DeleteSingleHomeUserResponseDTO> deleteResponses = new ArrayList<>();
        for (Long userId : deleteHomeUsersRequestDTO.getHomeUserIds()) {
            //TODO  Check for permission to delete this specific user
            if (userId.toString().isEmpty()) {
                return DeleteSingleHomeUserResponseDTO.responseError(DeleteSingleHomeUserResponseErrorType.INVALID_USER_ID);
            }

            var userOpt = homeUserRepository.findById(userId);

            // Check if current user is part of this home
            var currentUserHomeEntityOpt = abstractHomeUserEntityRepository.findByAppUserAppUserIdAndHomeHomeId(userId, homeId);
            if (currentUserHomeEntityOpt.isEmpty()) {
                return DeleteHomeUsersResponseDTO.responseError(DeleteHomeUsersResponseErrorType.NOT_PART_OF_THIS_HOME);
            }

            var response = new DeleteSingleHomeUserResponseDTO();
            response.setHomeUserId(userId);

            if (userOpt.isEmpty()) {
                return DeleteSingleHomeUserResponseDTO.responseError(DeleteSingleHomeUserResponseErrorType.USER_DOESNT_EXIST);
            } else {
                // Additional checks can be placed here (if needed) before deletion
                var homeUserAccessInfo = userOpt.get().getHomeUserAccessInfoEntity();

                if (homeUserAccessInfo.getHomeUserStatus() != HomeUserStatus.ACTIVE) {
                    return DeleteSingleHomeUserResponseDTO.responseError(DeleteSingleHomeUserResponseErrorType.USER_NOT_PART_OF_THIS_HOME);
                }

                homeUserAccessInfo.setDeletedFromHome(true);
                saveEntityThrows(homeUserAccessInfoRepository, homeUserAccessInfo);
                response.setDeleteSingleHomeUserResponseError(null);
            }

            deleteResponses.add(response);
        }

        var responseDTO = DeleteHomeUsersResponseDTO.builder()
                .deleteHomeUserResponses(deleteResponses)
                .build();

        return response(responseDTO);
    }

    private boolean endAccessForUser(HomeUserAccessInfoEntity homeUserAccessInfo) {
        try {
            homeUserAccessInfo.setAccessEndDate(LocalDateTime.now());
            saveEntityThrows(homeUserAccessInfoRepository, homeUserAccessInfo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
