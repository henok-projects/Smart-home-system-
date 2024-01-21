package com.galsie.gcs.homes.configuration.security.contexthelper;

import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.HomeUserStatus;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.homes.repository.home.HomeEntityRepository;
import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessCheckerService;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserHelperService;
import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityContextProvider;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.UserSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.lib.utils.pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class HomesGalSecurityContextHelper {

    @Autowired
    HomeEntityRepository homeEntityRepository; // TODO: add and use a gcsInternalFindHomeById in homes service, make it return an optional home entity

    @Autowired
    HomeRolesAndAccessCheckerService rolesAndAccessCheckerService;

    @Autowired
    HomeUserHelperService homeUserHelperService;

    /**
     * This method is used by service methods which are called by controllers with @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
     */
    public UserSecurityAuthSession getUserAuthSession() throws GCSResponseException {
        var userAuthSessionOpt = GalSecurityContextProvider.getContextualUserAuthSession();
        if (userAuthSessionOpt.isEmpty()) {
            throw new GCSResponseException(GCSResponseErrorType.AUTH_SESSION_NOT_FOUND);
        }
        return userAuthSessionOpt.get();
    }


    /**
     * NOTE: Accounts for all errors except INVALID_HOME_ID, this is accounted for as part of the DTO
     * @param homeId
     * @param permissions CAN be null
     * @param allowEvenIfUserIsDeleted
     * @param allowEvenIfUserHasLeft
     * @param allowEvenIfUserAccessEnded
     * @return
     * @throws GCSResponseException
     */
    public ContextualAuthenticatedHomeUserInfo getContextualHomeUserInfo(Long homeId, List<String> permissions, boolean allowEvenIfUserIsDeleted, boolean allowEvenIfUserHasLeft, boolean allowEvenIfUserAccessEnded) throws GCSResponseException{
        var userAuthSession = getUserAuthSession();
        var userId = userAuthSession.getUserId();

        var homeEntityOpt = homeEntityRepository.findByHomeId(homeId);
        if (homeEntityOpt.isEmpty()){
            return ContextualAuthenticatedHomeUserInfo.error(userAuthSession, null,null, HomeResponseErrorType.HOME_DOESNT_EXIST);
        }
        var homeEntity = homeEntityOpt.get();
        // First, check if the user is part of this home, if not, return the NOT_PART_OF_HOME_ERROR
        // We do this before checking if the home is disabled or deleted, so that if the user isn't part of the home, he doesn't have that info
        var homeUserEntityOpt = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId);
        if (homeUserEntityOpt.isEmpty()) {
            return ContextualAuthenticatedHomeUserInfo.error(userAuthSession, homeEntity,null, HomeResponseErrorType.NOT_PART_OF_THIS_HOME);
        }
        var homeUserEntity = homeUserEntityOpt.get();
        var accessInfoEntity = homeUserEntity.getHomeUserAccessInfoEntity();
        // Check if USER IS DELETED from home
        if (!allowEvenIfUserIsDeleted && accessInfoEntity.isDeletedFromHome()){
            return ContextualAuthenticatedHomeUserInfo.error(userAuthSession, homeEntity,homeUserEntity, HomeResponseErrorType.NOT_PART_OF_THIS_HOME);
        }
        // NOW check if the home is disabled or deleted
        switch(homeEntity.getStatus()){
            case DISABLED:
                return ContextualAuthenticatedHomeUserInfo.error(userAuthSession, homeEntity, homeUserEntity, HomeResponseErrorType.HOME_ISNT_ACTIVE);
            case DELETED:
                return ContextualAuthenticatedHomeUserInfo.error(userAuthSession, homeEntity, homeUserEntity, HomeResponseErrorType.HOME_IS_DELETED);
            default:
                break;
        }

        // NOW, check for access
        var accessHomeErrorOpt = accessInfoEntity.getHomeUserStatus().toHomeResponseErrorType(allowEvenIfUserHasLeft, allowEvenIfUserAccessEnded);
        if (accessHomeErrorOpt.isPresent()){
            return ContextualAuthenticatedHomeUserInfo.error(userAuthSession, homeEntity, homeUserEntity, accessHomeErrorOpt.get());
        }

        // Now check for permission
        if (permissions != null && !rolesAndAccessCheckerService.gcsInternalDoesHomeUserHavePermissions(homeUserEntity, permissions)){
            return ContextualAuthenticatedHomeUserInfo.error(userAuthSession, homeEntity, homeUserEntity, HomeResponseErrorType.NO_PERMISSION);
        }

        // Finally, return success
        return ContextualAuthenticatedHomeUserInfo.success(userAuthSession, homeEntity, homeUserEntity);
    }

    // TODO: Remove this, it shouldn't be necessary
    public Pair<HomeResponseErrorType, HomeUserEntity> getHomeUserEntity(Long userId, Long homeId){
        var homeUserEntityOpt = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId);
        if(homeUserEntityOpt.isEmpty()){
            return Pair.ofFirst(HomeResponseErrorType.NOT_PART_OF_THIS_HOME);
        }
        if(homeUserEntityOpt.get().getHomeUserAccessInfoEntity().isDeletedFromHome()){
            return Pair.ofFirst(HomeResponseErrorType.NOT_PART_OF_THIS_HOME);
        }
        if(homeUserEntityOpt.get().getHomeUserAccessInfoEntity().getHomeUserStatus().equals(HomeUserStatus.LEFT)){
            return Pair.ofFirst(HomeResponseErrorType.LEFT_HOME);
        }
        return Pair.ofSecond(homeUserEntityOpt.get());
    }



}
