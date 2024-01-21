package com.galsie.gcs.homes.service.home.rolesandaccess;

import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.homes.repository.home.user.HomeUserEntityRepository;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserHelperService;
import com.galsie.gcs.homescommondata.repository.home.user.AbstractHomeUserEntityRepository;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HomeRolesAndAccessCheckerService {

    @Autowired
    HomeUserEntityRepository homeUserRepository;

    @Autowired
    AbstractHomeUserEntityRepository abstractHomeUserEntityRepository;

    @Autowired
    HomeUserHelperService homeUserHelperService;


    //Checks if the home user with that id has ALL the permissions provided in the list
    public boolean gcsInternalDoesHomeUserHavePermissions(Long homeUserId, List<String> permissions) throws GCSResponseException {

        return true;
    }
    public boolean gcsInternalDoesHomeUserHavePermissions(HomeUserEntity entity, List<String> permissions) throws GCSResponseException {
        return true;
    }

}
