package com.galsie.gcs.users.service.verification;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.users.configuration.security.contexthelper.UserGalSecurityContextHelper;
import com.galsie.gcs.users.data.discrete.verification.OTPVerificationType;
import com.galsie.gcs.users.data.discrete.verification.twofa.Request2FAResponseErrorType;
import com.galsie.gcs.users.data.dto.verification.request.twofa.UserAccount2FARequestDTO;
import com.galsie.gcs.users.data.dto.verification.response.twofa.InternalRequest2FAResponseDTO;
import com.galsie.gcs.users.data.dto.verification.response.twofa.UserAccount2FAResponseDTO;
import com.galsie.gcs.users.data.entity.security.UserAuthSessionEntity;
import com.galsie.gcs.users.repository.UserAuthSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TwoFAVerificationService {

    @Autowired
    OTPVerificationService otpVerificationService;

    @Autowired
    UserAuthSessionRepository userAuthSessionRepository;


    @Autowired
    UserGalSecurityContextHelper securityContextHelper;


    public GCSResponse<UserAccount2FAResponseDTO> authenticatedRequestTwoFactorAuthentication(UserAccount2FARequestDTO userAccount2FARequestDTO){
        try{
            return gcsInternalAuthenticatedRequestTwoFactorAuthentication(userAccount2FARequestDTO);
        }catch (GCSResponseException ex){
            return ex.getGcsResponse(UserAccount2FAResponseDTO.class);
        }
    }

    @Transactional
    public GCSResponse<UserAccount2FAResponseDTO> gcsInternalAuthenticatedRequestTwoFactorAuthentication(UserAccount2FARequestDTO userAccount2FARequestDTO){
        // Get the UserSecurityAuthSession from the security context provider
        var authInfo = securityContextHelper.getAuthenticatedUserInfo();
        var userAuthSessionEntity = authInfo.getUserAuthSessionEntity();
        var userAccountEntity = userAuthSessionEntity.getUser();

        // If 2FA is not enabled, return an error indicating that
        if (!userAccountEntity.getSecurityPreferences().is2FAEnabled()){
            return UserAccount2FAResponseDTO.responseError(Request2FAResponseErrorType.TWO_FA_NOT_ENABLED);
        }
        // Request 2FA for this session
        var interRequest2FAResp = gcsInternalRequest2FA(userAuthSessionEntity, userAccount2FARequestDTO.getVerificationType());
        if (interRequest2FAResp.hasError()){ // if it was a GcsError, return it
            return GCSResponse.errorResponse(interRequest2FAResp.getGcsError());
        }
        // if the internal request failed due to some 2FA error, return that
        var interRequest2FAResponseDTO = interRequest2FAResp.getResponseData();
        if (interRequest2FAResponseDTO.getRequest2FAResponseError() != null){ // if the
            return UserAccount2FAResponseDTO.responseError(interRequest2FAResponseDTO.getRequest2FAResponseError());
        }
        return UserAccount2FAResponseDTO.responseSuccess(interRequest2FAResponseDTO.getUserAuthSessionEntity().getTwoFAOTPVerificationToken());
    }

    @Transactional
    public GCSResponse<InternalRequest2FAResponseDTO> gcsInternalRequest2FA(UserAuthSessionEntity userAuthSessionEntity, OTPVerificationType otpVerificationType){
        var otpVerificationResponse = otpVerificationService.gcsInternalRequestOTPVerificationForUserAccount(userAuthSessionEntity.getUser(), otpVerificationType);
        if (otpVerificationResponse.hasError()){
            return GCSResponse.errorResponse(otpVerificationResponse.getGcsError());
        }
        var otpVerificationResponseData = otpVerificationResponse.getResponseData();
        var otpVerificationErrorType = otpVerificationResponseData.getOtpVerificationRequestError();
        if (otpVerificationErrorType != null){
            return InternalRequest2FAResponseDTO.responseError(Request2FAResponseErrorType.fromOTPVerificationErrorType(otpVerificationErrorType));
        }
        var otpVerificationToken = otpVerificationResponseData.getOtpVerificationToken();
        userAuthSessionEntity.setTwoFAOTPVerificationToken(otpVerificationToken);
        GCSResponse.saveEntityThrows(userAuthSessionRepository, userAuthSessionEntity);
        return InternalRequest2FAResponseDTO.responseSuccess(userAuthSessionEntity);
    }
}
