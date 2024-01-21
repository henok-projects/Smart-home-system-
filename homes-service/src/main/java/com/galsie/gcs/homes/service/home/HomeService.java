package com.galsie.gcs.homes.service.home;

import com.galsie.gcs.homes.configuration.security.contexthelper.HomesGalSecurityContextHelper;
import com.galsie.gcs.homes.data.discrete.HomeStatus;
import com.galsie.gcs.homes.data.dto.addhome.request.AddHomeRequestDTO;
import com.galsie.gcs.homes.data.dto.addhome.response.AddHomeResponseDTO;
import com.galsie.gcs.homes.data.dto.common.BasicHomeInfoDTO;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.dto.gethomeInfo.request.GetBasicHomeInfoRequestDTO;
import com.galsie.gcs.homes.data.dto.gethomeInfo.response.GetBasicHomeInfoResponseDTO;
import com.galsie.gcs.homes.repository.home.HomeEntityRepository;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserHelperService;
import com.galsie.gcs.homes.infrastructure.rolesandaccess.roles.HomeDefaultRolesProvider;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.*;

@Service
public class HomeService {
    @Autowired
    HomeEntityRepository homeEntityRepository;
    @Autowired
    HomesGalSecurityContextHelper homesGalSecurityContextHelper;
    @Autowired
    HomeUserHelperService homeUserHelperService;
    @Autowired
    HomeDefaultRolesProvider homeDefaultRolesProvider;

    public GCSResponse<AddHomeResponseDTO> addHome(AddHomeRequestDTO addHomeRequestDTO) {
        try {
            return gcsInternalAddHome(addHomeRequestDTO);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(AddHomeResponseDTO.class);
        }
    }

    @Transactional
    GCSResponse<AddHomeResponseDTO> gcsInternalAddHome(AddHomeRequestDTO addHomeRequestDTO) throws GCSResponseException {
        // Validation check
        var errorType = addHomeRequestDTO.validate();
        if (errorType.isPresent()) {
            return AddHomeResponseDTO.responseError(errorType.get());
        }

        // Extract User ID from Auth Session
        var userId = homesGalSecurityContextHelper.getUserAuthSession().getUserId();

        // Convert DTO to HomeEntity and set roles
        var homeEntity = addHomeRequestDTO.toHomeEntity();


        // 1. Set up the default roles and status for the home entity.
        var homeRoleEntities = homeDefaultRolesProvider.getHomeDefaultRoles()
                .stream()
                .map(defaultRole -> defaultRole.toHomeRoleEntity(homeEntity))//TODO: here we are building roles without names or permissions attached to them
                .collect(Collectors.toList());
        homeEntity.setHomeRoleEntities(homeRoleEntities);
        homeEntity.setStatus(HomeStatus.ACTIVE);


        var userHomeAccessInfoDto = UserHomeAccessInfoDTO.builder().
                roles(new ArrayList<>()).
                accessStartDate(String.valueOf(LocalDateTime.now())).
                accessEndDate(null).
                build();

        var savedHomeEntity = saveEntityThrows(homeEntityRepository, homeEntity).getResponseData();
        homeUserHelperService.gcsInternalCreateHomeUserEntityOrUpdateAccessInfoForIt(userId, savedHomeEntity, userHomeAccessInfoDto);

        return AddHomeResponseDTO.responseSuccess(BasicHomeInfoDTO.fromHomeEntity(homeEntity));
    }

    /**
     * NOTE: Allows even if user is deleted, and even if the user access ended (so we can still show home in list), but disallows if user left
     * @param getBasicHomeInfoRequestDto
     * @return
     * @throws GCSResponseException
     */
    public GCSResponse<GetBasicHomeInfoResponseDTO> getBasicHomeInfo(GetBasicHomeInfoRequestDTO getBasicHomeInfoRequestDto) {
        try {
            return gcsInternalGetBasicHomeInfo(getBasicHomeInfoRequestDto);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(GetBasicHomeInfoResponseDTO.class);
        }
    }

    @Transactional
    private GCSResponse<GetBasicHomeInfoResponseDTO> gcsInternalGetBasicHomeInfo(GetBasicHomeInfoRequestDTO getBasicHomeInfoRequestDTO) throws GCSResponseException {
        var errorType = getBasicHomeInfoRequestDTO.validate();
        if (errorType.isPresent()) {
            return GetBasicHomeInfoResponseDTO.responseError(errorType.get());
        }

        // NOTE: Allows even if user is deleted (so we can still show home in list) and even if access ended, but disallows if user left
        var contextualAuthenticatedHomeUserInfo = homesGalSecurityContextHelper.getContextualHomeUserInfo(getBasicHomeInfoRequestDTO.getHomeId(), null, true, false, true);
        if (contextualAuthenticatedHomeUserInfo.hasError()){
            return GetBasicHomeInfoResponseDTO.responseError(contextualAuthenticatedHomeUserInfo.getHomeResponseError());
        }

        var homeEntity = contextualAuthenticatedHomeUserInfo.getHomeEntity();
        return GetBasicHomeInfoResponseDTO.responseSuccess(BasicHomeInfoDTO.fromHomeEntity(homeEntity));
    }
}


