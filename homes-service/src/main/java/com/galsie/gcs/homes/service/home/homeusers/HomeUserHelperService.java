package com.galsie.gcs.homes.service.home.homeusers;

import com.galsie.gcs.homes.data.discrete.HomeUserStatus;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeDirectUserInviteResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserInviteByCommon;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.rolesandaccess.HomeRoleEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserAccessInfoEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.homes.data.entity.home.user.preferences.HomeUserPreferencesEntity;
import com.galsie.gcs.homes.repository.home.HomeEntityRepository;
import com.galsie.gcs.homes.repository.home.user.HomeUserAccessInfoRepository;
import com.galsie.gcs.homes.repository.home.user.HomeUserEntityRepository;
import com.galsie.gcs.homes.repository.home.user.HomeUserPreferenceRepository;
import com.galsie.gcs.homes.infrastructure.rolesandaccess.roles.HomeDefaultRolesProvider;
import com.galsie.gcs.homescommondata.data.entity.home.user.AbstractHomeUserEntity;
import com.galsie.gcs.homescommondata.data.entity.user.AppUserRemoteEntity;
import com.galsie.gcs.homescommondata.repository.appuser.AppUserRemoteEntityRepository;
import com.galsie.gcs.homescommondata.repository.home.user.AbstractHomeUserEntityRepository;
import com.galsie.gcs.homescommondata.service.home.AbstractHomeUserEntityService;
import com.galsie.gcs.homescommondata.service.user.AppUserRemoteEntityService;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.lib.utils.pair.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.saveEntityThrows;

/**
 * Has GCS INTERNAL methods that are used by various service
 */
@Service
@Slf4j
public class HomeUserHelperService {
    @Autowired
    HomeEntityRepository homeEntityRepository;

    @Autowired
    HomeUserEntityRepository homeUserEntityRepository;

    @Autowired
    AbstractHomeUserEntityRepository abstractHomeUserEntityRepository;

    @Autowired
    AppUserRemoteEntityRepository appUserRemoteEntityRepository;

    @Autowired
    HomeUserPreferenceRepository homeUserPreferenceRepository;

    @Autowired
    HomeUserAccessInfoRepository homeUserAccessInfoRepository;

    @Autowired
    AppUserRemoteEntityService appUserRemoteEntityService;

    @Autowired
    HomeDefaultRolesProvider homeDefaultRolesProvider;

    @Autowired
    AbstractHomeUserEntityService abstractHomeUserEntityService;


    public Optional<HomeUserEntity> gcsInternalFindByUserIdAndHomeId(Long userId, Long homeId) {
        // Fetching data from the repository
        Optional<AbstractHomeUserEntity> abstractHomeUserEntityOptional = abstractHomeUserEntityRepository.findByAppUserAppUserIdAndHomeHomeId(userId, homeId);
        if (abstractHomeUserEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        AbstractHomeUserEntity abstractHomeUserEntity = abstractHomeUserEntityOptional.get();
        // Deep Null Checks
        if (abstractHomeUserEntity.getAppUser() == null || abstractHomeUserEntity.getAppUser().getAppUserId() == null) {
            return Optional.empty();
        }
        // Fetching the entity by ID
        return homeUserEntityRepository.findById(abstractHomeUserEntity.getUniqueId());
    }

    @Transactional
    public HomeUserEntity gcsInternalCreateHomeUserEntityOrUpdateAccessInfoForIt(Long userId, HomeEntity homeEntity, UserHomeAccessInfoDTO userHomeAccessInfoDTO) throws GCSResponseException {

        // 2. Get or create the AppUser.
        var appUser = appUserRemoteEntityService.gcsInternalGetOrCreateAppUserEntity(userId);

        var homeUserEntityOptional = gcsInternalFindByUserIdAndHomeId(userId, homeEntity.getHomeId());
        var homeUserEntity = homeUserEntityOptional.orElseGet(() -> {
            var newHomeUserEntity = HomeUserEntity.builder().joinedAt(LocalDateTime.now()).build();
            newHomeUserEntity.setAppUser(appUser);
            newHomeUserEntity.setHome(homeEntity);
            saveEntityThrows(homeUserEntityRepository, newHomeUserEntity);
            return newHomeUserEntity;
        });


        // 4. Set up user preferences for the HomeUser.
        var preferencesEntity = HomeUserPreferencesEntity.builder()
                .id(homeUserEntity.getUniqueId())
                .homeUser(homeUserEntity)
                .build();
        preferencesEntity = saveEntityThrows(homeUserPreferenceRepository, preferencesEntity).getResponseData();
        homeUserEntity.setHomeUserPreferences(preferencesEntity);


        List<HomeRoleEntity> homeRoleEntities = new ArrayList<>(homeEntity.getHomeRoleEntities());//TODO: shouldn't we at this point get the homes master role and add our newly created  home user access info to it?
        // 5. Set up user access information using the default role.

        var homeUserAccessInfoEntity = new HomeUserAccessInfoEntity();
        var homeUserAccessInfoEntityOptional = homeUserAccessInfoRepository.findByHomeUserEntity(homeUserEntity).stream()
                .filter(homeUserAccessInfoEntity1 -> homeUserAccessInfoEntity1.getHomeEntity().getHomeId().equals(homeEntity.getHomeId())).findFirst();

        //if the user already has access info, we update the home user status to active and remove the old roles and permissions
        if (homeUserAccessInfoEntityOptional.isPresent()) {
            homeUserAccessInfoEntity = homeUserAccessInfoEntityOptional.get();
        }else{
            homeUserAccessInfoEntity.setHomeUserEntity(homeUserEntity);
            homeUserAccessInfoEntity.setHomeEntity(homeEntity);
            saveEntityThrows(homeUserAccessInfoRepository, homeUserAccessInfoEntity);
        }
        homeUserAccessInfoEntity.setHomeUserStatus(HomeUserStatus.ACTIVE);

        //get required roles from home roles
        List<HomeRoleEntity> rolesForAccessInfo = new ArrayList<>();
        for(var roleId: userHomeAccessInfoDTO.getRoles()){
            var matchingRoleOpt = homeRoleEntities.stream().filter(role -> role.getId().equals(roleId)).findFirst();
            if(matchingRoleOpt.isPresent()){
                rolesForAccessInfo.add(matchingRoleOpt.get());
                continue;
            }
            log.warn("Role with id {} not found in home roles", roleId);
        }

        //Update the roles, access start and end dates and permissions for user access info entity
        homeUserAccessInfoEntity.setRoles(rolesForAccessInfo);
        homeUserAccessInfoEntity.setAccessStartDate(userHomeAccessInfoDTO.getAccessStartDate() == null ? LocalDateTime.now() : LocalDateTime.parse(userHomeAccessInfoDTO.getAccessStartDate()));
        homeUserAccessInfoEntity.setAccessEndDate(userHomeAccessInfoDTO.getAccessEndDate() == null ? null : LocalDateTime.parse(userHomeAccessInfoDTO.getAccessEndDate()));
        homeUserAccessInfoEntity.setWithPermissions(userHomeAccessInfoDTO.getPermissions());

        saveEntityThrows(homeUserAccessInfoRepository, homeUserAccessInfoEntity);

        homeUserEntity.setHomeUserAccessInfoEntity(homeUserAccessInfoEntity);
        return homeUserEntity;
    }


    // TODO: Account for the various booleans
    public List<HomeUserEntity> getHomeUsers(Long homeId, boolean includeDeletedUsers, boolean includeLeftUsers, boolean includeAccessEndedUsers) {
        // Retrieve the home entity by ID
        var homeEntity = homeEntityRepository.findById(homeId)
                .orElseThrow(() -> new IllegalArgumentException("No HomeEntity found with ID: " + homeId));

        var abstractHomeUserEntityId = homeEntity.getAbstractHomeUserEntities()
                .stream()
                .map(AbstractHomeUserEntity::getUniqueId)
                .collect(Collectors.toList());

        return homeUserEntityRepository.findAllById(abstractHomeUserEntityId);


    }

    private AbstractHomeUserEntity createAndSaveAbstractHomeUserEntity(Long userId, Long homeId) throws GCSResponseException {

        var appUser = AppUserRemoteEntity.builder().appUserId(userId).build();
        saveEntityThrows(appUserRemoteEntityRepository, appUser);
        var home = homeEntityRepository.findById(homeId)
                .orElseThrow(() -> new GCSResponseException(GCSResponseErrorType.ENTITY_NOT_FOUND));


        var homeUser = new HomeUserEntity();
        homeUser.setAppUser(appUser);
        homeUser.setHome(home);

        saveEntityThrows(abstractHomeUserEntityRepository, homeUser);

        saveEntityThrows(homeUserEntityRepository, homeUser);


        return homeUser;
    }

    //TODO: Fix implementation
    //this is meant to retrieve the username and information from
    public Optional<HomeUserEntity> findHomeUserEntityByUsername(String username) {
        return Optional.empty();
    }



    //TODO: to match trello card
    public Map<HomeDirectUserInviteByCommon, Pair<HomeDirectUserInviteResponseErrorType, HomeUserEntity>> aggregateHomeUserInvites(List<HomeDirectUserInviteByCommon> invites){
        return null;
    }


}
