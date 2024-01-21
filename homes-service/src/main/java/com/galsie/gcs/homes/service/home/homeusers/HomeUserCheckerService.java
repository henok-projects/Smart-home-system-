package com.galsie.gcs.homes.service.home.homeusers;

import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Checks if the home users meet certain conditions
 */
@Service
public class HomeUserCheckerService {

    @Autowired
    HomeUserHelperService homeUserHelperService;

    /**
     *
     * @return
     */
    public int getHomeUsersCount(boolean includeDeletedUsers, boolean includeLeftUsers, boolean includeAccessEndedUsers){
        return -1; // TODO: IMPLEMENT
    }

    public boolean isUserPartOfHome(Long userId, Long homeId) {
        var homeUser = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId);
        if (homeUser.isPresent()) {
            return true;
        }
        return false;
    }
    public boolean gcsInternalWillSomeOperatorsNoLongerWorkIfHomeUserLeft(HomeUserEntity homeUserEntity){
        return false; // we return false so that this never causes an issue when leaving a home , TODO when operator system ready, integrate into it
    }


    public boolean isLastUserInHome(Long userId, Long homeId) {
        var homeUser = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId);
        if (homeUser != null) {
            return true;
        }
        return false;
    }

    public boolean isOnlyHouseMaster(Long userId, Long homeId) {

        var homeUser = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId);

        if (homeUser != null) {
            return true;
        }
        return false;
    }
    public boolean areValidHomeUsers(List<Long> userIds, Long homeId) {
        for(Long userId : userIds) {
            var homeUser = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId);
            if (homeUser == null) {
                return false; // return false immediately if one user is not a valid home user
            }
        }
        return true; // return true only if all users in the list are valid home users
    }



    public boolean isUserHouseMaster(Long userId, Long homeId) {
        var homeUser = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId);
        if (homeUser != null) {
            return true;
        }
        return false;
    }


    public boolean doAutomationsOrScenesDependOnUser(Long userId, Long homeId) {
        var homeUser = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId);
        if (homeUser != null) {
            return true;
        }
        return false;
    }

}
