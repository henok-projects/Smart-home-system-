package com.galsie.gcs.users.service.register;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.users.data.discrete.UserAccountSecurityType;
import com.galsie.gcs.users.data.discrete.UserAccountStatus;
import com.galsie.gcs.users.data.discrete.registration.GCSRegistrationErrorType;
import com.galsie.gcs.users.data.dto.login.response.GalsieLoginResponseDTO;
import com.galsie.gcs.users.data.dto.registration.response.GCSRegistrationResponseDTO;
import com.galsie.gcs.users.data.dto.registration.request.register.RegisterNewGalsieAccountRequestDTO;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.data.entity.UserEmailEntity;
import com.galsie.gcs.users.data.entity.UserInfoEntity;
import com.galsie.gcs.users.data.entity.UserPhoneEntity;
import com.galsie.gcs.users.data.entity.security.GalUserSecurityEntity;
import com.galsie.gcs.users.data.entity.security.UserAccountSecurityPreferencesEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.EmailOTPVerificationEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.PhoneOTPVerificationEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.AccountOTPVerificationEntity;
import com.galsie.gcs.users.repository.UserAccountRepository;
import com.galsie.gcs.users.repository.UserEmailRepository;
import com.galsie.gcs.users.repository.UserInfoRepository;
import com.galsie.gcs.users.repository.UserPhoneRepository;
import com.galsie.gcs.users.repository.security.GalUserSecurityRepository;
import com.galsie.gcs.users.repository.security.UserAccountSecurityPreferencesRepository;
import com.galsie.gcs.users.repository.verification.OTPVerificationSessionEntityRepository;
import com.galsie.gcs.users.service.login.GalsieLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.*;

@Service
public class UserRegistrationService {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    GalUserSecurityRepository securityRepository;

    @Autowired
    UserAccountSecurityPreferencesRepository userSecurityPreferencesRepository;

    @Autowired
    UserEmailRepository userEmailRepository;

    @Autowired
    UserPhoneRepository userPhoneRepository;

    @Autowired
    UserExistenceService userExistenceService;

    /*
    OTP
     */
    @Autowired
    OTPVerificationSessionEntityRepository otpVerificationSessionEntityRepository;

    /*
    Login
    */
    @Autowired
    GalsieLoginService loginService;

    public GCSResponse<GCSRegistrationResponseDTO> registerNewGalUserAccount(RegisterNewGalsieAccountRequestDTO registerNewGalUserAccountDTO){
        try {
            return gcsInternalRegisterNewGalUserAccount(registerNewGalUserAccountDTO);
        } catch (GCSResponseException e) {
            return errorResponse(e.getGcsResponse().getGcsError());
        }
    }

    @Transactional // Transactional and throws for the sake of rollback
    public GCSResponse<GCSRegistrationResponseDTO> gcsInternalRegisterNewGalUserAccount(RegisterNewGalsieAccountRequestDTO registerNewGalUserAccountDTO) throws GCSResponseException {
        if (!registerNewGalUserAccountDTO.isUsernameValid()){
            return GCSRegistrationResponseDTO.responseError(GCSRegistrationErrorType.INVALID_USERNAME);
        }

        if (!registerNewGalUserAccountDTO.isHashedPwdValid()){
            return GCSRegistrationResponseDTO.responseError(GCSRegistrationErrorType.INVALID_PASSWORD);
        }

        if (userExistenceService.gcsInternalExistsUserWithUsername(registerNewGalUserAccountDTO.getUsername())){
            return GCSRegistrationResponseDTO.responseError(GCSRegistrationErrorType.USERNAME_TAKEN);
        }

        var otpVerificationSessionEntityOpt = otpVerificationSessionEntityRepository.findByVerificationToken(registerNewGalUserAccountDTO.getOtpVerificationToken());
        if (otpVerificationSessionEntityOpt.isEmpty()){
            return GCSRegistrationResponseDTO.responseError(GCSRegistrationErrorType.INVALID_OTP_TOKEN);
        }

        var otpVerificationSessionEntity = otpVerificationSessionEntityOpt.get();
        if (!otpVerificationSessionEntity.isActive()){
            return GCSRegistrationResponseDTO.responseError(GCSRegistrationErrorType.INACTIVE_OTP_TOKEN);
        }

        if (!otpVerificationSessionEntity.isVerified()){
            return GCSRegistrationResponseDTO.responseError(GCSRegistrationErrorType.UNVERIFIED_OTP_TOKEN);
        }

        var otpVerificationEntity = otpVerificationSessionEntity.getOtpVerificationEntity();

        // otp verification entity cant be for a user account
        if (otpVerificationEntity instanceof AccountOTPVerificationEntity){
            return GCSResponse.errorResponse(GCSResponseErrorType.NOT_SUPPORTED);
        }

        otpVerificationSessionEntity.setExpired(true); // expire after finally registering using it
        saveEntityThrows(otpVerificationSessionEntityRepository, otpVerificationSessionEntity);

        // build & save user account
        var userAccountEntity = UserAccountEntity.builder()
                .username(registerNewGalUserAccountDTO.getUsername())
                .accountStatus(UserAccountStatus.ACTIVE)
                .securityType(UserAccountSecurityType.GALSIE)
                .build();
        saveEntityThrows(userAccountRepository, userAccountEntity); // throws for rollback, flush so that the changes are reflected in userAccountEntity

        var userSecurityAccountEntity = GalUserSecurityEntity.builder().user(userAccountEntity).password(registerNewGalUserAccountDTO.getHashedPwd()).build();
        saveEntityThrows(securityRepository, userSecurityAccountEntity); // throws for rollback

        var userSecurityPreferencesEntity = UserAccountSecurityPreferencesEntity.builder().user(userAccountEntity).is2FAEnabled(false).build();
        saveEntityThrows(userSecurityPreferencesRepository, userSecurityPreferencesEntity);

        var userInfoEntity = UserInfoEntity.builder().user(userAccountEntity).build();
        saveEntityThrows(userInfoRepository, userInfoEntity); // throws for rollback

        // TODO: see if theres a better way to do this (the preferences entity is first saved then added to the user account, maybe in some way it can be directly added?)
        userAccountEntity.setSecurityPreferences(userSecurityPreferencesEntity);
        saveEntityThrows(userAccountRepository, userAccountEntity);
         // build credentials
        if (otpVerificationEntity instanceof PhoneOTPVerificationEntity phoneOTPVerificationEntity){
            short countryCode = phoneOTPVerificationEntity.getCountryCode();
            String phoneNumber = phoneOTPVerificationEntity.getPhoneNumber();
            if (userPhoneRepository.findByCountryCodeAndPhoneNumber(countryCode, phoneNumber).isPresent()){
                return GCSRegistrationResponseDTO.responseError(GCSRegistrationErrorType.PHONE_ALREADY_REGISTERED);
            }
            var phoneEntity = UserPhoneEntity.builder().user(userAccountEntity).countryCode(countryCode).phoneNumber(phoneNumber).build();
            saveEntityThrows(userPhoneRepository, phoneEntity); // throws for rollback
        }else if (otpVerificationEntity instanceof EmailOTPVerificationEntity emailOTPVerificationEntity){
            String email = emailOTPVerificationEntity.getEmail();
            if (userEmailRepository.findByEmail(email).isPresent()){
                return GCSRegistrationResponseDTO.responseError(GCSRegistrationErrorType.EMAIL_ALREADY_REGISTERED);
            }
            var emailEntity = UserEmailEntity.builder().user(userAccountEntity).email(email).build();
            saveEntityThrows(userEmailRepository, emailEntity); // throws for rollback
        }
        GCSResponse<GalsieLoginResponseDTO> gcsLoginResponse;
        try {
            gcsLoginResponse = loginService.gcsInternalDoLoginUser(userAccountEntity);
        }catch (GCSResponseException exception){
            return exception.getGcsResponse(GCSRegistrationResponseDTO.class);
        }
        return GCSRegistrationResponseDTO.responseSuccess(gcsLoginResponse.getResponseData());
    }


    @Transactional
    public GCSResponse<GCSRegistrationResponseDTO> registerNewGoogleAccount(){
        return errorResponse(GCSResponseErrorType.NOT_IMPLEMENTED);
    }

    @Transactional
    public GCSResponse<GCSRegistrationResponseDTO> registerNewAppleAccount(){
        return errorResponse(GCSResponseErrorType.NOT_IMPLEMENTED);
    }

}
