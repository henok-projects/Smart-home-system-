package com.galsie.gcs.homes.service.home;

import com.galsie.gcs.homes.configuration.security.contexthelper.HomesGalSecurityContextHelper;
import com.galsie.gcs.homes.data.discrete.HomeStatus;
import com.galsie.gcs.homes.data.discrete.deletehome.DeleteHomeResponseErrorType;
import com.galsie.gcs.homes.data.dto.deletehome.request.DeleteHomeRequestDTO;
import com.galsie.gcs.homes.data.dto.deletehome.response.DeleteHomeResponseDTO;
import com.galsie.gcs.homes.repository.home.HomeEntityRepository;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserCheckerService;
import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessCheckerService;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.saveEntityThrows;

@Service
public class DeleteHomeService {
    @Autowired
    HomeEntityRepository homeEntityRepository;
    @Autowired
    HomesGalSecurityContextHelper homesGalSecurityContextHelper;

    @Autowired
    HomeUserCheckerService homeUserCheckerService;

    @Autowired
    HomeRolesAndAccessCheckerService rolesAndPermissionsService;

    public GCSResponse<DeleteHomeResponseDTO> deleteHome(DeleteHomeRequestDTO homeDeleteRequestDto) {
        try {
            return gcsInternalDeleteHome(homeDeleteRequestDto);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(DeleteHomeResponseDTO.class);
        }
    }

    GCSResponse<DeleteHomeResponseDTO> gcsInternalDeleteHome(DeleteHomeRequestDTO homeDeleteRequestDto) throws GCSResponseException {

        // Validate the request
        var validationError = homeDeleteRequestDto.validate();
        if (validationError.isPresent()) {
            return DeleteHomeResponseDTO.responseError(null, validationError.get());
        }

        // CHECK PERMISSION
        var homeId = homeDeleteRequestDto.getHomeId();
        var contextualAuthenticatedHomeUserInfo = homesGalSecurityContextHelper.getContextualHomeUserInfo(homeId, List.of("home.management.delete.full"), false, false, false);
        if(contextualAuthenticatedHomeUserInfo.hasError()){
            return DeleteHomeResponseDTO.responseError(contextualAuthenticatedHomeUserInfo.getHomeResponseError(), null);
        }

        if (!homeDeleteRequestDto.isForceOperation()){
            if (homeUserCheckerService.getHomeUsersCount(false, false, false) > 1){
                return DeleteHomeResponseDTO.responseError(null, DeleteHomeResponseErrorType.USERS_PART_OF_THIS_HOME_WILL_BE_REMOVED);
            }
            return DeleteHomeResponseDTO.responseError(null, DeleteHomeResponseErrorType.CONFIRMATION_REQUIRED);
        }
        var homeEntity = contextualAuthenticatedHomeUserInfo.getHomeEntity();
        homeEntity.setStatus(HomeStatus.DELETED);
        saveEntityThrows(homeEntityRepository, homeEntity);

        return DeleteHomeResponseDTO.responseSuccess();
    }


}
