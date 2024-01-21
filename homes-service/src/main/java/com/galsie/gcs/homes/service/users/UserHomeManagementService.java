package com.galsie.gcs.homes.service.users;

import com.galsie.gcs.homes.configuration.security.contexthelper.HomesGalSecurityContextHelper;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.HomeStatus;
import com.galsie.gcs.homes.data.discrete.HomeUserStatus;
import com.galsie.gcs.homes.data.discrete.deletehome.DeleteHomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homesystemroletype.HomeSystemRoleType;
import com.galsie.gcs.homes.data.discrete.leavehome.LeaveHomeResponseErrorType;
import com.galsie.gcs.homes.data.dto.archivehome.request.ArchiveHomeSetRequestDTO;
import com.galsie.gcs.homes.data.dto.archivehome.response.ArchiveHomeSetResponseDTO;
import com.galsie.gcs.homes.data.dto.archivehome.response.ArchiveSingleHomeResponseDTO;
import com.galsie.gcs.homes.data.dto.deletehome.request.DeleteHomeRequestDTO;
import com.galsie.gcs.homes.data.dto.deletehome.response.DeleteHomeResponseDTO;
import com.galsie.gcs.homes.data.dto.leavehome.request.LeaveHomeSetRequestDTO;
import com.galsie.gcs.homes.data.dto.leavehome.request.LeaveSingleHomeDTO;
import com.galsie.gcs.homes.data.dto.leavehome.response.LeaveSingleHomeResponseDTO;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.homes.repository.home.HomeEntityRepository;
import com.galsie.gcs.homes.repository.home.user.HomeUserEntityRepository;
import com.galsie.gcs.homes.repository.homerole.HomeRoleEntityRepository;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserCheckerService;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserHelperService;
import com.galsie.gcs.homescommondata.repository.home.user.AbstractHomeUserEntityRepository;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.*;
import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.saveEntityThrows;

/**
 * A USER MANAGING THEIR HOME LIST
 * - Archive
 * - Leave
 */
@Service
public class UserHomeManagementService {

    @Autowired
    HomeEntityRepository homeEntityRepository;

    @Autowired
    HomeUserEntityRepository homeUserRepository;

    @Autowired
    HomeRoleEntityRepository homeRoleEntityRepository;



    @Autowired
    HomesGalSecurityContextHelper homesGalSecurityContextHelper;

    @Autowired
    HomeUserHelperService homeUserHelperService;
    @Autowired
    HomeUserCheckerService homeUserCheckerService;


    public GCSResponse<ArchiveHomeSetResponseDTO> archiveHome(ArchiveHomeSetRequestDTO archiveHomeRequestDTO) throws GCSResponseException {

        // TODO get user id from security context
        List<ArchiveSingleHomeResponseDTO> archiveResponses = new ArrayList<>();
        var homeIds = archiveHomeRequestDTO.getHomeIds();
        var userId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeEntities = homeEntityRepository.findAllById(homeIds);
        List<HomeUserEntity> homeUserEntitiesToSave = new ArrayList<>();
        Map<Long, HomeEntity> homeEntityMap = new LinkedHashMap<>();
        homeIds.forEach(homeId ->{
            homeEntityMap.put(homeId, homeEntities.stream().filter(homeEntity -> homeEntity.getHomeId().equals(homeId)).findFirst().orElse(null));
        });
        for(var entry : homeEntityMap.entrySet()){
            if(entry.getKey() < 1){
                archiveResponses.add(ArchiveSingleHomeResponseDTO.error(entry.getKey(), HomeResponseErrorType.INVALID_HOME_ID));
                continue;
            }
            if(entry.getValue() == null){
                archiveResponses.add(ArchiveSingleHomeResponseDTO.error(entry.getKey(), HomeResponseErrorType.HOME_DOESNT_EXIST));
                continue;
            }
            switch (entry.getValue().getStatus()){
                case DELETED:
                    archiveResponses.add(ArchiveSingleHomeResponseDTO.error(entry.getKey(), HomeResponseErrorType.HOME_IS_DELETED));
                    continue;
                case DISABLED:
                    archiveResponses.add(ArchiveSingleHomeResponseDTO.error(entry.getKey(), HomeResponseErrorType.HOME_ISNT_ACTIVE));
                    continue;
                default:
                    break;
            }
            var pairResponse = homesGalSecurityContextHelper.getHomeUserEntity(userId, entry.getKey());
            if(pairResponse.hasFirst()){
                archiveResponses.add(ArchiveSingleHomeResponseDTO.error(entry.getKey(), pairResponse.getFirst()));
                continue;
            }
            var homeUserEntity = pairResponse.getSecond();
            homeUserEntity.setHomeArchivedForUser(true);
            homeUserEntitiesToSave.add(homeUserEntity);
            archiveResponses.add(ArchiveSingleHomeResponseDTO.success(entry.getKey()));
        }

        var archiveHomeEntitiesResponse = saveEntities(homeUserRepository, homeUserEntitiesToSave);
        if (archiveHomeEntitiesResponse.hasError()) {
            errorResponse(archiveHomeEntitiesResponse.getGcsError());
        }
        return response(new ArchiveHomeSetResponseDTO(archiveResponses));
    }


    public GCSResponse<List<LeaveSingleHomeResponseDTO>> leaveHome(LeaveHomeSetRequestDTO homeLeaveRequestDto) throws GCSResponseException {
        var userId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();
        var homeLeaves = homeLeaveRequestDto.getHomesToLeave();

        var leaveResponses = homeLeaves.stream()
                .map(homeLeave -> gcsInternalLeaveSingleHome(userId, homeLeave))
                .collect(Collectors.toList());

        return response(leaveResponses);
    }

    private LeaveSingleHomeResponseDTO gcsInternalLeaveSingleHome(Long userId, LeaveSingleHomeDTO leaveSingleHomeDTO) throws GCSResponseException {
        var homeId = leaveSingleHomeDTO.getHomeId();
        var homeEntityOptional = homeEntityRepository.findById(homeId);

        if (homeEntityOptional.isEmpty()) {
            return LeaveSingleHomeResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST, null, homeId).getResponseData();
        }

        if (homeEntityOptional.get().getStatus() != HomeStatus.ACTIVE){
            return LeaveSingleHomeResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null, homeId).getResponseData();
        }

        var homeEntity = homeEntityOptional.get();
        var homeUser = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(homeId, userId);
        if (homeUser.isEmpty()) {
            return LeaveSingleHomeResponseDTO.responseError(HomeResponseErrorType.NOT_PART_OF_THIS_HOME, null, homeId).getResponseData();
        }

        // Handle last user scenario
        if (homeUserCheckerService.isLastUserInHome(userId, homeId)) {
            if (leaveSingleHomeDTO.getAllowDeleteIfLastUser() == null || !leaveSingleHomeDTO.getAllowDeleteIfLastUser()) {
                // If allow_delete_if_last_user is false or null, respond with HOME_WILL_BE_DELETED
                return LeaveSingleHomeResponseDTO.responseError(null, LeaveHomeResponseErrorType.HOME_WILL_BE_DELETED, homeId).getResponseData();
            } else {
                // If allow_delete_if_last_user is true, mark the home as deleted but we proceed to leave home successfully.
                // TODO: persist homeEntity deletion status
                homeEntity.setStatus(HomeStatus.DELETED);
                saveEntityThrows(homeEntityRepository, homeEntity);
                return LeaveSingleHomeResponseDTO.success(homeId);
            }
        }


        // IF some operators will no longer work, return an error
        if (!leaveSingleHomeDTO.shouldLeaveEvenIfSomeOperatorsWouldntWork() && homeUserCheckerService.gcsInternalWillSomeOperatorsNoLongerWorkIfHomeUserLeft(homeUser.get())) {
            return LeaveSingleHomeResponseDTO.responseError(null, LeaveHomeResponseErrorType.SOME_OPERATORS_WILL_NO_LONGER_WORK, homeId).getResponseData();
        }

        // Handle housemaster scenario
        if (isUserOnlyHouseMasterAndNoValidNewHousemasters(userId, homeId, leaveSingleHomeDTO.getNewHousemasterHomeUserIds())) {
            return LeaveSingleHomeResponseDTO.responseError(null, LeaveHomeResponseErrorType.NEED_TO_SELECT_A_NEW_HOUSEMASTER, homeId).getResponseData();
        }

        // TODO: Potentially fix this
        if (homeUserCheckerService.isUserHouseMaster(userId, homeId) && homeUserCheckerService.isOnlyHouseMaster(userId, homeId)) {
            var newHousemasters = leaveSingleHomeDTO.getNewHousemasterHomeUserIds();
            if (newHousemasters != null && !newHousemasters.isEmpty() && homeUserCheckerService.areValidHomeUsers(newHousemasters, homeId)) {
                for (Long houseMaster : newHousemasters) {
                    for (HomeRoleEntity role : homeUser.get().getHomeRoleEntity()) {

                        if (homeUser.get().getUserId().equals(houseMaster))
                            role.setName(String.valueOf(HomeSystemRoleType.HOUSEMASTER)); // Set isHouseMaster to true

                        if (homeUser.get().getUserId().equals(houseMaster))
                            role.setName(String.valueOf(HomeSystemRoleType.HOUSEMASTER)); // Set isHouseMaster to true

                    }
                    homeRoleEntityRepository.saveAll(homeUser.get().getHomeRoleEntity());
                }
            }
        }

        homeUser.get().getHomeUserAccessInfoEntity().setHomeUserStatus(HomeUserStatus.LEFT);
        saveEntityThrows(homeUserRepository, homeUser.get());

        return LeaveSingleHomeResponseDTO.success(homeId);
    }

    private boolean isUserOnlyHouseMasterAndNoValidNewHousemasters(Long userId, Long homeId, List<Long> newHouseMasterIds) {
        if (!homeUserCheckerService.isUserHouseMaster(userId, homeId) || !homeUserCheckerService.isOnlyHouseMaster(userId, homeId)) {
            return false;
        }
        return newHouseMasterIds == null || newHouseMasterIds.isEmpty() || !homeUserCheckerService.areValidHomeUsers(newHouseMasterIds, userId);
    }


}
