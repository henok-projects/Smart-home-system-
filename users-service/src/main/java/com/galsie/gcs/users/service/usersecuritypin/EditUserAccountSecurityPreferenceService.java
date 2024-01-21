package com.galsie.gcs.users.service.usersecuritypin;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.users.configuration.security.contexthelper.UserGalSecurityContextHelper;
import com.galsie.gcs.users.data.discrete.editprofileinfo.EditUserInfoVerificationErrorType;
import com.galsie.gcs.users.data.discrete.usersecuritypin.GetUserSecurityAppPinResponseErrorType;
import com.galsie.gcs.users.data.discrete.usersecuritypin.SetupUserSecurity2FAResponseErrorType;
import com.galsie.gcs.users.data.discrete.usersecuritypin.SetupUserSecurityAppPinResponseErrorType;
import com.galsie.gcs.users.data.dto.editprofileinfo.pin.request.SetupUserSecurityAppPinRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.pin.response.get.GetUserSecurityPinResponseDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.pin.response.setup.SetupUserSecurityPinResponseDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.twofactorauth.request.SetupUserSecurity2FARequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.twofactorauth.response.SetupUserSecurity2FAResponseDTO;
import com.galsie.gcs.users.data.entity.security.UserAccountSecurityPreferencesEntity;
import com.galsie.gcs.users.repository.security.UserAccountSecurityPreferencesRepository;
import com.galsie.gcs.users.repository.verification.OTPVerificationSessionEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class EditUserAccountSecurityPreferenceService {

    @Autowired
    UserGalSecurityContextHelper userGalSecurityContextHelper;
    @Autowired
    UserAccountSecurityPreferencesRepository userAccountSecurityPreferencesRepository;
    @Autowired
    OTPVerificationSessionEntityRepository otpVerificationSessionEntityRepository;

    public GCSResponse<SetupUserSecurityPinResponseDTO> requestSetUpPin(SetupUserSecurityAppPinRequestDTO request) throws GCSResponseException {
        try {
            return gcsInternalRequestSetUpPin(request);
        } catch (GCSResponseException e){
            return e.getGcsResponse(SetupUserSecurityPinResponseDTO.class);
        }
    }

    @Transactional
    public GCSResponse<SetupUserSecurityPinResponseDTO> gcsInternalRequestSetUpPin(SetupUserSecurityAppPinRequestDTO request) throws GCSResponseException {
        try {
            var errorType1 = request.validateToken();
            var errorType2 = request.validateHashedPin();

            if (errorType1.isPresent() || errorType2.isPresent()) {
                return SetupUserSecurityPinResponseDTO.responseError(null, errorType2.orElse(null));
            }

            // Extract User ID from Auth Session
            var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();


            // Extract and verify the editing verification token
            var otpVerificationSessionEntityOptional = otpVerificationSessionEntityRepository.findByVerificationToken(request.getEditingVerificationToken());

            if (otpVerificationSessionEntityOptional.isEmpty()){
                return SetupUserSecurityPinResponseDTO.responseError(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN,null);
            }
            var otpVerificationSessionEntity = otpVerificationSessionEntityOptional.get();
            if (!otpVerificationSessionEntity.isActive()){
                return SetupUserSecurityPinResponseDTO.responseError(EditUserInfoVerificationErrorType.INACTIVE_EDITING_VERIFICATION_TOKEN,null);
            }

            if (!otpVerificationSessionEntity.isVerified()){
                return SetupUserSecurityPinResponseDTO.responseError(EditUserInfoVerificationErrorType.UNVERIFIED_EDITING_VERIFICATION_TOKEN,null);
            }

            boolean isPinUpdated = updateHashedPin(userId, request.getHashedPin());

            if (isPinUpdated) {
                return SetupUserSecurityPinResponseDTO.responseSuccess();
            } else {
                return SetupUserSecurityPinResponseDTO.responseError(null, SetupUserSecurityAppPinResponseErrorType.INVALID_PIN);
            }
        } catch (Exception e) {
            // Handle exceptions here
            return SetupUserSecurityPinResponseDTO.responseError(EditUserInfoVerificationErrorType.UNVERIFIED_EDITING_VERIFICATION_TOKEN, null);
        }
    }

    public GCSResponse<SetupUserSecurity2FAResponseDTO> requestSetUp2FA(SetupUserSecurity2FARequestDTO request) throws GCSResponseException {
        try {
            return gcsInternalRequestSetUp2FA(request);
        } catch (GCSResponseException e){
            return e.getGcsResponse(SetupUserSecurity2FAResponseDTO.class);
        }
    }

    @Transactional
    public GCSResponse<SetupUserSecurity2FAResponseDTO> gcsInternalRequestSetUp2FA(SetupUserSecurity2FARequestDTO request) throws GCSResponseException {
        try {
            var errorType1 = request.validateToken();
            var errorType2 = request.validate2FAEnabled();

            if (errorType1.isPresent() || errorType2.isPresent()) {
                return SetupUserSecurity2FAResponseDTO.responseError(errorType1.orElse(null), errorType2.orElse(null));
            }

            // Extract User ID from Auth Session
            var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();


            // Extract and verify the editing verification token
            var otpVerificationSessionEntityOptional = otpVerificationSessionEntityRepository.findByVerificationToken(request.getEditingVerificationToken());

            if (otpVerificationSessionEntityOptional.isEmpty()){
                return SetupUserSecurity2FAResponseDTO.responseError(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN,null);
            }

            var otpVerificationSessionEntity = otpVerificationSessionEntityOptional.get();
            if (!otpVerificationSessionEntity.isActive()){
                return SetupUserSecurity2FAResponseDTO.responseError(EditUserInfoVerificationErrorType.INACTIVE_EDITING_VERIFICATION_TOKEN,null);
            }

            if (!otpVerificationSessionEntity.isVerified()){
                return SetupUserSecurity2FAResponseDTO.responseError(EditUserInfoVerificationErrorType.UNVERIFIED_EDITING_VERIFICATION_TOKEN,null);
            }

            boolean is2FAUpdated = updateUser2FAPreference(userId, request.getEnable2FA());
            if (is2FAUpdated) {
                return SetupUserSecurity2FAResponseDTO.responseSuccess();
            } else {
                return SetupUserSecurity2FAResponseDTO.responseError(null, SetupUserSecurity2FAResponseErrorType.INVALID_2FA_VALUE);
            }
        } catch (Exception e) {
            // Handle exceptions here
            return SetupUserSecurity2FAResponseDTO.responseError(EditUserInfoVerificationErrorType.UNVERIFIED_EDITING_VERIFICATION_TOKEN, null);
        }
    }

    public GCSResponse<GetUserSecurityPinResponseDTO> requestGetPin() throws GCSResponseException {
        try {
            return gcsInternalRequestGetPin();
        } catch (GCSResponseException e){
            return e.getGcsResponse(GetUserSecurityPinResponseDTO.class);
        }
    }
    @Transactional
    public GCSResponse<GetUserSecurityPinResponseDTO> gcsInternalRequestGetPin() throws GCSResponseException {
        try {

            // Extract User ID from Auth Session
            var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();
            var userPin = requestGetPin(userId);
            if(userPin!=null){
                return GetUserSecurityPinResponseDTO.responseSuccess(userPin);
            }
            else {
                return GetUserSecurityPinResponseDTO.responseError(GetUserSecurityAppPinResponseErrorType.PIN_NOT_SETUP);
            }

        } catch (Exception e) {
            // Handle exceptions here
            return GetUserSecurityPinResponseDTO.responseError(GetUserSecurityAppPinResponseErrorType.PIN_NOT_SETUP);
        }
    }
    private boolean updateHashedPin(Long userId, String newHashedPin) {
        Optional<UserAccountSecurityPreferencesEntity> userAccountSecurityPreferencesEntity = userAccountSecurityPreferencesRepository.findById(userId);
        if(userAccountSecurityPreferencesEntity.isPresent()){
            UserAccountSecurityPreferencesEntity userSecurityEntity = userAccountSecurityPreferencesEntity.get();
            userSecurityEntity.setHashedAppPin(newHashedPin);
            userAccountSecurityPreferencesRepository.save(userSecurityEntity);
            return true;
        }
        return false;
    }
    private boolean updateUser2FAPreference(Long userId, boolean enable2FA) {
        Optional<UserAccountSecurityPreferencesEntity> userAccountSecurityPreferencesEntity = userAccountSecurityPreferencesRepository.findById(userId);
        if(userAccountSecurityPreferencesEntity.isPresent()){
            UserAccountSecurityPreferencesEntity userSecurityEntity = userAccountSecurityPreferencesEntity.get();
            userSecurityEntity.set2FAEnabled(enable2FA);
            userAccountSecurityPreferencesRepository.save(userSecurityEntity);
            return true;
        }
        return false;
    }
    private String requestGetPin(Long userId) {
        Optional<UserAccountSecurityPreferencesEntity> userSecurityPreferences = userAccountSecurityPreferencesRepository.findById(userId);
        if(userSecurityPreferences.isPresent() && userSecurityPreferences.get().getHashedAppPin()!=null){
            return userSecurityPreferences.get().getHashedAppPin();
        }
        return null;
    }


}
