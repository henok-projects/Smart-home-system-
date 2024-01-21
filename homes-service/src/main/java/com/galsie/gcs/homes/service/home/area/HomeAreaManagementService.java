package com.galsie.gcs.homes.service.home.area;

import com.galsie.gcs.homes.configuration.security.contexthelper.HomesGalSecurityContextHelper;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addarea.AddHomeAreaResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addarea.adddoor.AddAreaDoorResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addarea.addwindow.AddAreaWindowResponseErrorType;
import com.galsie.gcs.homes.data.dto.addarea.request.AddHomeAreaRequestDTO;
import com.galsie.gcs.homes.data.dto.addarea.response.AddHomeAreaResponseDTO;
import com.galsie.gcs.homes.data.dto.adddoor.request.AddAreaDoorRequestDTO;
import com.galsie.gcs.homes.data.dto.adddoor.response.AddAreaDoorResponseDTO;
import com.galsie.gcs.homes.data.dto.addwindow.request.AddAreaWindowRequestDTO;
import com.galsie.gcs.homes.data.dto.addwindow.response.AddAreaWindowResponseDTO;
import com.galsie.gcs.homes.data.dto.common.DoorDTO;
import com.galsie.gcs.homes.data.dto.common.WindowDTO;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.door.HomeDoorEntity;
import com.galsie.gcs.homes.data.entity.home.windows.HomeWindowEntity;
import com.galsie.gcs.homes.repository.home.HomeEntityRepository;
import com.galsie.gcs.homes.repository.homearea.HomeAreaEntityRepository;
import com.galsie.gcs.homes.repository.homearea.details.HomeAreaDetailsRepository;
import com.galsie.gcs.homes.repository.homearea.door.HomeDoorRepository;
import com.galsie.gcs.homes.repository.homearea.window.HomeWindowRepository;
import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessCheckerService;
import com.galsie.gcs.microservicecommon.lib.galassets.GCSGalAssetsProvidedDTOCacheService;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.areaconfig.AreaConfigurationProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.galsie.gcs.homes.data.dto.addarea.response.AddHomeAreaResponseDTO.responseSuccess;
import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.saveEntityThrows;


@Service
public class HomeAreaManagementService {

    @Autowired
    HomeAreaEntityRepository homeAreaEntityRepository;

    @Autowired
    HomeAreaDetailsRepository homeAreaDetailsRepository;

    @Autowired
    HomeDoorRepository homeDoorRepo;

    @Autowired
    HomeWindowRepository homeWindowRepo;

    @Autowired
    HomeEntityRepository homeEntityRepository;

    @Autowired
    HomesGalSecurityContextHelper homesGalSecurityContextHelper;

    @Autowired
    GCSGalAssetsProvidedDTOCacheService gcsGalAssetsProvidedDTOCacheService;


    @Autowired
    HomeRolesAndAccessCheckerService rolesAndPermissionsService;



    public GCSResponse<AddHomeAreaResponseDTO> addHomeArea(AddHomeAreaRequestDTO addAreaRequestDTO) throws GCSResponseException {
        try {
            return gcsInternalAddArea(addAreaRequestDTO);
        }catch (GCSResponseException e)
        {
            return e.getGcsResponse(AddHomeAreaResponseDTO.class);
        }
    }

    @Transactional
    GCSResponse<AddHomeAreaResponseDTO> gcsInternalAddArea(AddHomeAreaRequestDTO addHomeAreaRequestDTO) throws GCSResponseException {

        // Retrieve User ID and Home ID
        var homeId = addHomeAreaRequestDTO.getHomeId();
        if(homeId == null || homeId <= 0){
            return AddHomeAreaResponseDTO.responseError(HomeResponseErrorType.INVALID_HOME_ID, null);
        }

        // Permission check
        var contextualAuthenticatedHomeUserInfo = homesGalSecurityContextHelper.getContextualHomeUserInfo(homeId, List.of("home.management.areas.full"), false, false, false);
        if(contextualAuthenticatedHomeUserInfo.hasError()){
            return AddHomeAreaResponseDTO.responseError(contextualAuthenticatedHomeUserInfo.getHomeResponseError(), null);
        }

        // Validate the request DTO
        var errorType = addHomeAreaRequestDTO.validate();
        if (errorType.isPresent()) {
            return AddHomeAreaResponseDTO.responseError(null, errorType.get());
        }

        var  areaConfigurationDTOOpt = gcsGalAssetsProvidedDTOCacheService.getProvidableAssetDTO(ProvidableAssetDTOType.AREA_CONFIGURATION_CONTENT, AreaConfigurationProvidedAssetDTO.class);
        if(areaConfigurationDTOOpt.isEmpty()){
            return GCSResponse.errorResponse(GCSResponseErrorType.GCS_REMOTE_REQUEST_FAILED);
        }

        var areaConfigurationDTO = areaConfigurationDTOOpt.get();
        if(areaConfigurationDTO.getInitialColors().containsKey(addHomeAreaRequestDTO.getAreaDetails().getInitialsColor())){
            return AddHomeAreaResponseDTO.responseError(null, AddHomeAreaResponseErrorType.INVALID_COLOR);
        }

        var homeAreaDetailsEntities = homeAreaDetailsRepository.findAllByName(addHomeAreaRequestDTO.getAreaDetails().getAreaName());
        if(homeAreaDetailsEntities.stream().anyMatch(homeAreaDetail -> Objects.equals(homeAreaDetail.getHomeAreaEntity().getHome().getHomeId(), homeId))){
            return AddHomeAreaResponseDTO.responseError(null, AddHomeAreaResponseErrorType.AREA_NAME_ALREADY_EXISTS);
        }

        var homeEntity = contextualAuthenticatedHomeUserInfo.getHomeEntity();
        if(addHomeAreaRequestDTO.getFloorId() != null){
            var homeFloorEntityOpt = homeEntity.getHomeFloors().stream().filter(homeFloorEntity -> homeFloorEntity.getFloorNumber().equals(addHomeAreaRequestDTO.getFloorId())).findFirst();
            if(homeFloorEntityOpt.isEmpty()){
                return AddHomeAreaResponseDTO.responseError(null, AddHomeAreaResponseErrorType.INVALID_FLOOR);
            }
        }
        // Save the area entity if permissions are valid
        var homeAreaEntity = addHomeAreaRequestDTO.toHomeAreaEntity(homeEntity, areaConfigurationDTOOpt.get());
        saveEntityThrows(homeAreaEntityRepository, homeAreaEntity);

        return responseSuccess(AddHomeAreaResponseDTO.AreaBasicInfoFromEntity(homeAreaEntity));
    }

    public Optional<HomeEntity> getHomeEntityById(Long id) {
        return homeEntityRepository.findById(id);
    }


    public GCSResponse<AddAreaDoorResponseDTO> addHomeAreaDoor(AddAreaDoorRequestDTO addAreaDoorRequestDTO) {
        try {
            return gcsInternalAddHomeAreaDoor(addAreaDoorRequestDTO);
        }catch (GCSResponseException e)
        {
            return e.getGcsResponse(AddAreaDoorResponseDTO.class);
        }
    }

    @Transactional
    private GCSResponse<AddAreaDoorResponseDTO> gcsInternalAddHomeAreaDoor(AddAreaDoorRequestDTO addAreaDoorRequestDTO) throws GCSResponseException{
        var validationError = addAreaDoorRequestDTO.validate();
        if(validationError.isPresent()){
            return AddAreaDoorResponseDTO.responseError(null, validationError.get());
        }
        var homeAreaEntityOpt = homeAreaEntityRepository.findById(addAreaDoorRequestDTO.getAreaId());
        if(homeAreaEntityOpt.isEmpty()){
            return AddAreaDoorResponseDTO.responseError(null, AddAreaDoorResponseErrorType.AREA_DOES_NOT_EXIST);
        }
        var homeAreaEntity = homeAreaEntityOpt.get();
        var contextualAuthenticatedHomeUserInfo = homesGalSecurityContextHelper.getContextualHomeUserInfo(homeAreaEntity.getHome().getHomeId(),
                List.of("home.management.areas.full"), false, false, false);
        if(!contextualAuthenticatedHomeUserInfo.hasError()){
            return AddAreaDoorResponseDTO.responseError(contextualAuthenticatedHomeUserInfo.getHomeResponseError(), null);
        }
        var homeAreaDoorEntity = HomeDoorEntity.builder().connectsFromArea(homeAreaEntity).doorName(addAreaDoorRequestDTO.getDoorName()).build();
        if(addAreaDoorRequestDTO.getConnectToAreaId() != null){
            var homeAreaEntityConnectToOpt = homeAreaEntityRepository.findById(addAreaDoorRequestDTO.getConnectToAreaId());
            if(homeAreaEntityConnectToOpt.isEmpty()){
                return AddAreaDoorResponseDTO.responseError(null, AddAreaDoorResponseErrorType.CONNECT_TO_AREA_DOES_NOT_EXIST);
            }
            var homeAreaEntityConnectTo = homeAreaEntityConnectToOpt.get();
            if(!homeAreaEntityConnectTo.getHome().getHomeId().equals(homeAreaEntity.getHome().getHomeId())){
                return AddAreaDoorResponseDTO.responseError(null, AddAreaDoorResponseErrorType.AREA_AND_CONNECT_TO_AREA_NOT_IN_SAME_HOME);
            }
            homeAreaDoorEntity.setConnectsToArea(addAreaDoorRequestDTO.getConnectToAreaId());
        }
        saveEntityThrows(homeDoorRepo, homeAreaDoorEntity);
        var areaDoors = homeAreaEntity.getDoors();
        areaDoors.add(homeAreaDoorEntity);
        homeAreaEntity.setDoors(areaDoors);
        saveEntityThrows(homeAreaEntityRepository, homeAreaEntity);
        return AddAreaDoorResponseDTO.responseSuccess(DoorDTO.fromEntity(homeAreaDoorEntity));
    }

    public GCSResponse<AddAreaWindowResponseDTO> addHomeAreaWindow(AddAreaWindowRequestDTO addAreaWindowRequestDTO) {
        try {
            return gcsInternalAddHomeAreaWindow(addAreaWindowRequestDTO);
        }catch (GCSResponseException e)
        {
            return e.getGcsResponse(AddAreaWindowResponseDTO.class);
        }
    }

    @Transactional
    private GCSResponse<AddAreaWindowResponseDTO> gcsInternalAddHomeAreaWindow(AddAreaWindowRequestDTO addAreaWindowRequestDTO) throws GCSResponseException {
        var validationError = addAreaWindowRequestDTO.validate();
        if(validationError.isPresent()){
            return AddAreaWindowResponseDTO.responseError(null, validationError.get());
        }
        var homeAreaEntityOpt = homeAreaEntityRepository.findById(addAreaWindowRequestDTO.getAreaId());
        if(homeAreaEntityOpt.isEmpty()){
            return AddAreaWindowResponseDTO.responseError(null, AddAreaWindowResponseErrorType.AREA_DOES_NOT_EXIST);
        }
        var homeAreaEntity = homeAreaEntityOpt.get();
        var contextualAuthenticatedHomeUserInfo = homesGalSecurityContextHelper.getContextualHomeUserInfo(homeAreaEntity.getHome().getHomeId(),
                List.of("home.management.areas.full"), false, false, false);
        if(!contextualAuthenticatedHomeUserInfo.hasError()){
            return AddAreaWindowResponseDTO.responseError(contextualAuthenticatedHomeUserInfo.getHomeResponseError(), null);
        }
        var homeAreaWindowEntity = HomeWindowEntity.builder().homeAreaEntity(homeAreaEntity).name(addAreaWindowRequestDTO.getWindowName()).build();
        saveEntityThrows(homeWindowRepo, homeAreaWindowEntity);
        var areaWindows = homeAreaEntity.getWindows();
        areaWindows.add(homeAreaWindowEntity);
        homeAreaEntity.setWindows(areaWindows);
        saveEntityThrows(homeAreaEntityRepository, homeAreaEntity);
        return AddAreaWindowResponseDTO.responseSuccess(WindowDTO.fromEntity(homeAreaWindowEntity));
    }
}
