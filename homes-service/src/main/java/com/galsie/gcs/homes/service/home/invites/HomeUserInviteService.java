package com.galsie.gcs.homes.service.home.invites;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galsie.gcs.homes.configuration.security.contexthelper.HomesGalSecurityContextHelper;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.HomeStatus;
import com.galsie.gcs.homes.data.discrete.HomeUserStatus;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeDirectUserInviteResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeDirectUserSetInviteResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeQRUserInviteResponseErrorType;
import com.galsie.gcs.homes.data.discrete.joinhome.JoinHomeResponseErrorType;
import com.galsie.gcs.homes.data.dto.adddoor.response.AddAreaDoorResponseDTO;
import com.galsie.gcs.homes.data.dto.common.CreationInfoDTO;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.homes.data.dto.getreceivedinvites.GetReceivedUserInvitesResponseDTO;
import com.galsie.gcs.homes.data.dto.invites.request.qr.QRCodeInviteRequestDTO;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserInviteByCommon;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserInviteByEmailOrPhoneDTO;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserInviteByUsernameDTO;
import com.galsie.gcs.homes.data.dto.invites.request.user.HomeDirectUserSetInviteRequestDTO;
import com.galsie.gcs.homes.data.dto.invites.response.qr.HomeQRUserInviteDataDTO;
import com.galsie.gcs.homes.data.dto.invites.response.qr.HomeQRUserInviteResponseDTO;
import com.galsie.gcs.homes.data.dto.invites.response.user.HomeDirectUserInviteDataDTO;
import com.galsie.gcs.homes.data.dto.invites.response.user.HomeDirectUserSetInviteDataDTO;
import com.galsie.gcs.homes.data.dto.invites.response.user.HomeDirectUserSetInviteResponseDTO;
import com.galsie.gcs.homes.data.dto.joinhome.response.JoinHomeResponseDTO;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.homes.data.entity.home.invites.EmailOrPhoneBasedHomeInviteEntity;
import com.galsie.gcs.homes.data.entity.home.invites.HomeInviteEntity;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.homes.repository.home.HomeEntityRepository;
import com.galsie.gcs.homes.repository.home.user.HomeUserInviteEntityRepository;
import com.galsie.gcs.homes.repository.phone.PhoneNumberRepository;
import com.galsie.gcs.homes.service.home.rolesandaccess.HomeRolesAndAccessCheckerService;
import com.galsie.gcs.homes.service.home.homeusers.HomeUserHelperService;
import com.galsie.gcs.homes.infrastructure.rolesandaccess.permissions.format.HomePermissionsFormatter;
import com.galsie.gcs.homes.service.users.UserHomeNotificationPreferenceService;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.*;


@Service
public class HomeUserInviteService {
    @Autowired
    HomeUserInviteEntityRepository homeUserInviteEntityRepository;
    @Autowired
    HomeEntityRepository homeEntityRepository;
    @Autowired
    HomesGalSecurityContextHelper homesGalSecurityContextHelper;
    @Autowired
    PhoneNumberRepository phoneNumberRepository;
    @Autowired
    HomeRolesAndAccessCheckerService rolesAndPermissionsService;
    @Autowired
    HomeUserHelperService homeUserHelperService;

    @Autowired
    HomePermissionsFormatter homePermissionsFormatter;

    @Autowired
    HomeInviteCodeService homeInviteCodeService;
    @Autowired
    UserHomeNotificationPreferenceService notificationPreferenceService;

    @Autowired
    HomeUserInvitesNotifierService homeUserInvitesNotifierService;

    @Transactional
    public GCSResponse<HomeDirectUserSetInviteResponseDTO> requestPerformHomeUserInvite(HomeDirectUserSetInviteRequestDTO homeDirectUserSetInviteRequestDTO) throws GCSResponseException {

        //CHECK AND VALIDATE HOME INFO
        var homeId = homeDirectUserSetInviteRequestDTO.getHomeId();
        if(homeId == null || homeId <= 0) {
            return HomeDirectUserSetInviteResponseDTO.responseError(HomeResponseErrorType.INVALID_HOME_ID, null, null);
        }
        var contextualAuthenticatedHomeUserInfo = homesGalSecurityContextHelper.getContextualHomeUserInfo(homeId, List.of("home.management.general.info.custom.3.full"), false, false, false);
        if(contextualAuthenticatedHomeUserInfo.hasError()){
            return HomeDirectUserSetInviteResponseDTO.responseError(contextualAuthenticatedHomeUserInfo.getHomeResponseError(), null, null);
        }

        //VALIDATE DATE AND IT'S FORMAT
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        var startDate = homeDirectUserSetInviteRequestDTO.getAccessInfo().getAccessStartDate();
        var endDate = homeDirectUserSetInviteRequestDTO.getAccessInfo().getAccessEndDate();
        var fStartDate = LocalDateTime.parse(startDate, formatter);
        var fEndDate = LocalDateTime.parse(endDate, formatter);

        if (startDate.isEmpty() || endDate.isEmpty()) {
            return HomeDirectUserSetInviteResponseDTO.responseError(null, null,HomeDirectUserSetInviteResponseErrorType.INVALID_DATES);
        }

        if (!(fStartDate.isBefore(fEndDate))) {
            return HomeDirectUserSetInviteResponseDTO.responseError(null,null,HomeDirectUserSetInviteResponseErrorType.START_DATE_MUST_PRECEED_END_DATE);
        }

        // Check DOES THE REQUEST IS VALID
        var error = homeDirectUserSetInviteRequestDTO.validate();
        if (error.isPresent()) {
            return HomeDirectUserSetInviteResponseDTO.responseError(null, error.get(), null);
        }
      
        //GET ACCESS INFO FROM REQUEST
        var accessInfoDTO = homeDirectUserSetInviteRequestDTO.getAccessInfo();

        //GENERATE INVITE CODE BECAUSE INVITE CODE IS THE SAME FOR ALL INVITES
        var inviteCodeOpt = homeInviteCodeService.gcsInternalGenerateUniqueInviteCode(false); // Direct user invite
        if (inviteCodeOpt.isEmpty()) {
            HomeDirectUserSetInviteResponseDTO.responseError(null, HomeQRUserInviteResponseErrorType.INVITE_CODE_GENERATION_REACHED_MAX_RETRIES, null);
        }
        var inviteCode = inviteCodeOpt.get();

        //CREATE HomeDirectUserSetInviteDataDTO and set access info
        var homeDirectUserSetInviteDataDTO = HomeDirectUserSetInviteDataDTO.builder().userAccessInfo(accessInfoDTO).build();

        //WE WILL STORE ALL INVITES IN THIS LIST
        List<HomeDirectUserInviteDataDTO> homeDirectUserInviteDataDTOList = new ArrayList<>();
        for (HomeDirectUserInviteByCommon homeDirectUserInviteByCommon : homeDirectUserSetInviteRequestDTO.getInviteList()) {

            var inviteAccessInfoEntity = UserHomeAccessInfoDTO.toInviteAccessInfoEntity(accessInfoDTO);
            var roles = UserHomeAccessInfoDTO.toInviteAccessInfoEntity(accessInfoDTO).getRoles();

            // Formatting and filtering permissions
            var permissions = homeDirectUserSetInviteRequestDTO.getAccessInfo().getPermissions().stream().toList();

            var homeEntity = contextualAuthenticatedHomeUserInfo.getHomeEntity();

            if (homeDirectUserInviteByCommon instanceof HomeDirectUserInviteByUsernameDTO homeDirectUserInviteByUsernameDTO) {
                homeDirectUserInviteDataDTOList.add(gcsInternalProcessInviteByUsername(homeDirectUserInviteByUsernameDTO,  homeEntity, accessInfoDTO, inviteCode));
            } else if (homeDirectUserInviteByCommon instanceof HomeDirectUserInviteByEmailOrPhoneDTO homeDirectUserInviteByEmailOrPhoneDTO) {
                gcsInternalProcessInviteByEmailOrPhone(homeDirectUserInviteByEmailOrPhoneDTO,  homeEntity, accessInfoDTO, inviteCode);
            }
        }

        //HAVING BUILT THE LIST OF INVITES, WE CREATE AND SET THE REMAINING INFORMATION IN HomeDirectUserSetInviteDataDTO
        var creationInfo = CreationInfoDTO.builder().createdOn(LocalDateTime.now()).build();
        homeDirectUserSetInviteDataDTO.setCreationInfo(creationInfo);
        homeDirectUserSetInviteDataDTO.setInviteUniqueCode(inviteCode);
        homeDirectUserSetInviteDataDTO.setInviteList(homeDirectUserInviteDataDTOList);
        //RETURN RESPONSE
        return HomeDirectUserSetInviteResponseDTO.responseSuccess(homeDirectUserSetInviteDataDTO);
    }

    private HomeDirectUserInviteDataDTO gcsInternalProcessInviteByUsername(HomeDirectUserInviteByUsernameDTO inviteByUsername,
                                                                           HomeEntity homeEntity, UserHomeAccessInfoDTO accessInfoDTO, String inviteCode) throws GCSResponseException {
        //Get Username from request and get the user's details. Then use the invite code passed as a parameter
        homeUserHelperService.findHomeUserEntityByUsername(inviteByUsername.getUsername());

        // No need for validation, if the user is not found exception in HomeDirectUserInviteDataDTO.error
        var homeInviteEntity = inviteByUsername.toGalsieUserAccountHomeInviteEntity(homeEntity, accessInfoDTO);
        homeInviteEntity.setInviteUniqueCode(inviteCode);

        var savedInvite = saveEntityThrows(homeUserInviteEntityRepository, homeInviteEntity);
        var homeUserInvite = HomeDirectUserSetInviteResponseDTO.builder()
                        .inviteData(HomeDirectUserSetInviteDataDTO.fromHomeInviteEntity(savedInvite.getResponseData()))
                        .build();
        return HomeDirectUserInviteDataDTO.error(HomeDirectUserInviteResponseErrorType.USER_NOT_FOUND);

    }

    private HomeDirectUserInviteDataDTO gcsInternalProcessInviteByEmailOrPhone(HomeDirectUserInviteByEmailOrPhoneDTO inviteByEmailOrPhone,
                                                                               HomeEntity homeEntity, UserHomeAccessInfoDTO accessInfoDTO, String inviteCode) throws GCSResponseException {
        //Get email or phone from request and get the user's details. Then use the invite code passed as a parameter
        var homeInviteEntity = inviteByEmailOrPhone.toInviteUserByEmailOrPhoneEntity(homeEntity, accessInfoDTO);

        // No need for validation, if the user is not found or missing details exception in HomeDirectUserInviteDataDTO.error
        var phoneNumber = saveEntityThrows(phoneNumberRepository, homeInviteEntity.getPhoneNumber()).getResponseData();
                    homeInviteEntity.setPhoneNumber(phoneNumber);
                    homeInviteEntity.setInviteUniqueCode(inviteCode);

        var savedInvite = saveEntityThrows(homeUserInviteEntityRepository, homeInviteEntity);
        String inviteLink = "gcs.galsie.com/homes/home/onboarding/join?code=" + savedInvite.getResponseData().getInviteUniqueCode();

        if (inviteByEmailOrPhone.getEmail() != null && !inviteByEmailOrPhone.getEmail().isEmpty()) {
            homeUserInvitesNotifierService.sendEmailToInvitedUser(inviteByEmailOrPhone.getEmail(), savedInvite.getResponseData().getInviteUniqueCode(), inviteLink);
        }
        // TODO: integrate with sms. For now, this does nothing. Once integration is ready, fill out this method.
        var homeUserInvite = HomeDirectUserSetInviteResponseDTO.builder()
                .inviteData(HomeDirectUserSetInviteDataDTO.fromHomeInviteEntity(savedInvite.getResponseData()))
                .build();

        return HomeDirectUserInviteDataDTO.error(HomeDirectUserInviteResponseErrorType.USER_NOT_FOUND);
    }

    @Transactional
    public GCSResponse<?> requestCreateHomeQRInvite(QRCodeInviteRequestDTO qrCodeInviteRequestDTO) throws GCSResponseException {

        Long homeId = qrCodeInviteRequestDTO.getHomeId();

        var homeEntityOptional = homeEntityRepository.findByHomeId(homeId);
        if (homeEntityOptional.isEmpty()){
            return HomeDirectUserSetInviteResponseDTO.responseError(HomeResponseErrorType.HOME_DOESNT_EXIST, null, null);
        }

        if (homeEntityOptional.get().getStatus() != HomeStatus.ACTIVE){
            return HomeDirectUserSetInviteResponseDTO.responseError(HomeResponseErrorType.HOME_ISNT_ACTIVE, null, null);
        }

        var contextualAuthenticatedHomeUserInfo = homesGalSecurityContextHelper.getContextualHomeUserInfo(homeEntityOptional.get().getHomeId(),
                List.of("home.management.areas.full"), false, false, false);
        if(!contextualAuthenticatedHomeUserInfo.hasError()){
            return AddAreaDoorResponseDTO.responseError(contextualAuthenticatedHomeUserInfo.getHomeResponseError(), null);
        }

        var error = qrCodeInviteRequestDTO.validate();
        if (error.isPresent()) {
            return HomeQRUserInviteResponseDTO.responseError(error.get());
        }

        Optional<String> inviteCode = homeInviteCodeService.gcsInternalGenerateUniqueInviteCode(true); // QR code invite
        if (inviteCode.isEmpty()) {
            return HomeQRUserInviteResponseDTO.responseError(HomeQRUserInviteResponseErrorType.INVITE_CODE_GENERATION_REACHED_MAX_RETRIES);
        }

        var homeInviteEntity = qrCodeInviteRequestDTO.toQRBasedHomeInviteEntity(homeEntityOptional.get());
        homeInviteEntity.setInviteUniqueCode(inviteCode.get());
        var saveInvitedUserEntityResponse = saveEntityThrows(homeUserInviteEntityRepository, homeInviteEntity);

        if (saveInvitedUserEntityResponse.hasError()) {
            return errorResponse(saveInvitedUserEntityResponse.getGcsError());
        }

        return response(HomeQRUserInviteDataDTO.fromQRCodeHomeInviteDataEntity(saveInvitedUserEntityResponse.getResponseData()));

    }


    @Transactional
    public GCSResponse<?> getReceivedInvites() throws GCSResponseException {

        var homeInviteList = homeUserInviteEntityRepository.findAll();

        var receivedUserInviteResponseDataDTO = GetReceivedUserInvitesResponseDTO.builder()
                .receivedInviteList(GetReceivedUserInvitesResponseDTO.fromListOfHomeInviteEntity(homeInviteList))
                .build();

        return response(GetReceivedUserInvitesResponseDTO.builder().receivedInviteList(receivedUserInviteResponseDataDTO.getReceivedInviteList()).build());

    }

    public GCSResponse<?> joinHome(String inviteUniqueCode) {
        try {
            return gcsInternalJoinHome(inviteUniqueCode);
        } catch (GCSResponseException e) {
            return e.getGcsResponse(JoinHomeResponseDTO.class);
        }
    }

    @Transactional
    GCSResponse<?> gcsInternalJoinHome(String inviteUniqueCode) throws GCSResponseException {

        var userSecurityAuthSession = homesGalSecurityContextHelper.getUserAuthSession();
        var userId = userSecurityAuthSession.getUserId();
        var response = new JoinHomeResponseDTO();

        // 1. Validate the invite code
        if (!homeInviteCodeService.gcsInternalIsInviteCodeValid(inviteUniqueCode)) {
            return JoinHomeResponseDTO.responseError(JoinHomeResponseErrorType.INVALID_INVITE_CODE);
        }

        Optional<HomeInviteEntity> inviteOptional = homeUserInviteEntityRepository.findByInviteUniqueCode(inviteUniqueCode);
        if (inviteOptional.isPresent()) {
            var homeEntity = inviteOptional.get().getHomeEntity();

            // var now = LocalDateTime.now();
            var accessExpiry = inviteOptional.get().getAccessInfo().getAccessEndDate();

            // Check if user is already part of the home
            if (isUserPartOfHome(homeEntity.getHomeId(), userId)) {
                return JoinHomeResponseDTO.responseError(JoinHomeResponseErrorType.USER_ALREADY_PART_OF_HOME);
            }

            if (homeEntity.getStatus() != HomeStatus.ACTIVE) {
                return JoinHomeResponseDTO.responseError(JoinHomeResponseErrorType.HOME_IS_INACTIVE);
            }

            // Direct Invite Logic and QR Invite Logic
            if (inviteUniqueCode.startsWith("U") || inviteUniqueCode.startsWith("Q")) {
                // Check if the user has already joined using this invite
                if (hasUserJoinedThenLeft(userId, homeEntity.getHomeId())) {
                    return JoinHomeResponseDTO.responseError(JoinHomeResponseErrorType.USER_ALREADY_JOINED_THEN_LEFT_THROUGH_THIS_INVITE);
                }
            } else if (accessExpiry != null && accessExpiry.isBefore(LocalDateTime.now())) {
                return JoinHomeResponseDTO.responseError(JoinHomeResponseErrorType.INVITATION_EXPIRED);
            } else {
                response.setHomeInfoDTO(JoinHomeResponseDTO.fromEntity(homeEntity));
            }

            var withPermissions = inviteOptional.get().getAccessInfo().getWithPermissions().stream()
                    .map(homePermissionsFormatter::format)
                    .filter(Optional::isPresent)
                    .map(Optional::get).toList();

            var invitedUserId = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeEntity.getHomeId()).get().getUserId();
            var contextualAuthenticatedHomeUserInfo = homesGalSecurityContextHelper.getContextualHomeUserInfo(inviteOptional.get().getHomeEntity().getHomeId(),
                    List.of("home.management.join.full"), false, false, false);
            if(!contextualAuthenticatedHomeUserInfo.hasError()){
                return AddAreaDoorResponseDTO.responseError(contextualAuthenticatedHomeUserInfo.getHomeResponseError(), null);
            }

            var userEntity = homeUserHelperService.gcsInternalCreateHomeUserEntityOrUpdateAccessInfoForIt(userId, homeEntity, null);
            response = JoinHomeResponseDTO.builder()
                    .homeInfoDTO(JoinHomeResponseDTO.fromEntity(homeEntity))
                    .build();
            //send email and SMS
            try {
                homeUserInvitesNotifierService.notifyHomeUsersThatNewUserJoined(homeEntity.getHomeId(), userEntity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return response(response);
    }



    public EmailOrPhoneBasedHomeInviteEntity getInviteByUser(HomeUserEntity user) {
        return homeUserInviteEntityRepository.findByInvitor(user).orElse(null);
    }

    private boolean hasUserJoinedThenLeft(Long userId, Long homeId) {
        var homeUserOpt = homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId);
        return homeUserOpt.isPresent() && homeUserOpt.get().getHomeUserAccessInfoEntity().getHomeUserStatus() == HomeUserStatus.LEFT;
    }

    private boolean isUserPartOfHome(Long homeId, Long userId) {
        return homeUserHelperService.gcsInternalFindByUserIdAndHomeId(userId, homeId).isPresent();
    }

}