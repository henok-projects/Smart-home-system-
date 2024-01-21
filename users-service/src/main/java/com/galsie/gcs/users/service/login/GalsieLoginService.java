package com.galsie.gcs.users.service.login;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManagerCommonImpl;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.users.data.discrete.UserAccountStatus;
import com.galsie.gcs.users.data.discrete.login.GalsieLoginResponseErrorType;
import com.galsie.gcs.users.data.discrete.verification.OTPVerificationType;
import com.galsie.gcs.users.data.dto.login.request.GalsieEmailLoginRequestDTO;
import com.galsie.gcs.users.data.dto.login.request.GalsieLoginRequestDTO;
import com.galsie.gcs.users.data.dto.login.request.GalsiePhoneLoginRequestDTO;
import com.galsie.gcs.users.data.dto.login.request.GalsieUsernameLoginRequestDTO;
import com.galsie.gcs.users.data.dto.login.response.GalsieLoginDataDTO;
import com.galsie.gcs.users.data.dto.login.response.GalsieLoginResponseDTO;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.data.entity.security.UserAuthSessionEntity;
import com.galsie.gcs.users.events.UserAuthSessionStartedGCSEvent;
import com.galsie.gcs.users.repository.UserAccountRepository;
import com.galsie.gcs.users.repository.UserAuthSessionRepository;
import com.galsie.gcs.users.repository.UserEmailRepository;
import com.galsie.gcs.users.repository.UserPhoneRepository;
import com.galsie.gcs.users.repository.security.GalUserSecurityRepository;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.CodableUserAuthSessionToken;
import com.galsie.gcs.users.service.verification.TwoFAVerificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.*;

@Service
public class GalsieLoginService {

    static Logger logger = LogManager.getLogger();

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    GalUserSecurityRepository securityRepository;

    @Autowired
    UserEmailRepository userEmailRepository;

    @Autowired
    UserPhoneRepository userPhoneRepository;

    @Autowired
    UserAuthSessionRepository userAuthSessionRepository;

    @Autowired
    TwoFAVerificationService twoFAVerificationService;

    @Autowired
    GCSEventManagerCommonImpl globalEventManager;

    public GCSResponse<GalsieLoginResponseDTO> loginUser(GalsieLoginRequestDTO loginRequestDTO) {
        UserAccountEntity user;
        if (loginRequestDTO instanceof GalsieUsernameLoginRequestDTO usernameLoginRequestDTO){
            var username = usernameLoginRequestDTO.getUsername();
            var userAccount = userAccountRepository.findByUsername(username);
            if (userAccount.isEmpty()){
                return GalsieLoginResponseDTO.responseError(GalsieLoginResponseErrorType.INVALID_CREDENTIALS);
            }
            user = userAccount.get();
        }else if (loginRequestDTO instanceof GalsieEmailLoginRequestDTO emailLoginRequestDTO){
            var email = emailLoginRequestDTO.getEmail();
            var userEmail = userEmailRepository.findByEmail(email);
            if (userEmail.isEmpty()){
                return GalsieLoginResponseDTO.responseError(GalsieLoginResponseErrorType.INVALID_CREDENTIALS);
            }
            user = userEmail.get().getUser();
        }else if (loginRequestDTO instanceof GalsiePhoneLoginRequestDTO dto){
            var userPhone = userPhoneRepository.findByCountryCodeAndPhoneNumber(dto.getCountryCode(), dto.getPhoneNumber());
            if (userPhone.isEmpty()){
                return GalsieLoginResponseDTO.responseError(GalsieLoginResponseErrorType.INVALID_CREDENTIALS);
            }
            user = userPhone.get().getUser();
        }else{
            return GalsieLoginResponseDTO.responseError(GalsieLoginResponseErrorType.INVALID_CREDENTIALS);
        }
        // definitely have a user here
        var pwd = loginRequestDTO.getHashedPwd();
        var securityEntity = securityRepository.findByIdAndPassword(user.getId(), pwd); // user id matches security entity id
        if (securityEntity.isEmpty()){
            return GalsieLoginResponseDTO.responseError(GalsieLoginResponseErrorType.INVALID_CREDENTIALS);
        }
        try {
            return gcsInternalDoLoginUser(user);
        }catch (GCSResponseException gcsResponseException){
            return gcsResponseException.getGcsResponse(GalsieLoginResponseDTO.class);
        }
    }


    /**
     * TO ONLY BE USED BY OTHER SERVICES, NOT BY CONTROLLER
     * @param user
     * @return
     * @throws GCSResponseException: For the sake of rollback, but still carrying a GCSResponse
     */
    @Transactional
    public GCSResponse<GalsieLoginResponseDTO> gcsInternalDoLoginUser(UserAccountEntity user) throws GCSResponseException { // package-private
        /*
        Fail if the account is disabled or deleted
         */
        if (user.getAccountStatus() == UserAccountStatus.DISABLED){
            return GalsieLoginResponseDTO.responseError(GalsieLoginResponseErrorType.ACCOUNT_DISABLED);
        }else if (user.getAccountStatus() == UserAccountStatus.DELETED){
            return GalsieLoginResponseDTO.responseError(GalsieLoginResponseErrorType.ACCOUNT_DELETED);
        }
        /*
        Create the session entity
         */
        var sessionEntity = UserAuthSessionEntity.builder()
                .user(user)
                .authToken("")
                .lastAccess(LocalDateTime.now())
                .validUntil(LocalDateTime.now().plusHours(UserAuthSessionEntity.SESSION_VALIDITY_HOURS)).build();
        saveEntityThrows(userAuthSessionRepository, sessionEntity);
        /*
        Create the token and save it to the session entity
         */
        var userAuthSessionToken = new CodableUserAuthSessionToken(sessionEntity.getId(), user.getId(), user.getUsername());
        sessionEntity.setSessionToken(userAuthSessionToken.toStringToken());
        saveEntityThrows(userAuthSessionRepository, sessionEntity);

        /*
        If 2FA is enabled, perform a 2fa request
         */
        if (user.getSecurityPreferences().is2FAEnabled()){
            var tfaResponse = twoFAVerificationService.gcsInternalRequest2FA(sessionEntity, OTPVerificationType.DEFAULT);
            if (tfaResponse.hasError()){
                throw new GCSResponseException(errorResponse(tfaResponse.getGcsError()));
            }
            var internalRequest2FAResponseDTO = tfaResponse.getResponseData();
            if (internalRequest2FAResponseDTO.getRequest2FAResponseError() != null){
                logger.error("FAILED to send 2FA otp for the user " + user.getUsername() + " reason: " + internalRequest2FAResponseDTO.getRequest2FAResponseError());
            }else {
                sessionEntity = internalRequest2FAResponseDTO.getUserAuthSessionEntity(); // get the updated session entity with the
            }
        }
        var event = globalEventManager.callEvent(new UserAuthSessionStartedGCSEvent(userAuthSessionToken));
        if (event.isCancelled()){
            throw new GCSResponseException(GalsieLoginResponseDTO.responseError(GalsieLoginResponseErrorType.LOGIN_CANCELLED));
        }

        return GalsieLoginResponseDTO.responseSuccess(new GalsieLoginDataDTO(user.getUsername(), sessionEntity.getSessionToken(), sessionEntity.getTwoFAOTPVerificationToken()));
    }
}
