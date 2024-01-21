package com.galsie.gcs.users.service.verification;

import com.galsie.gcs.microservicecommon.lib.email.GCSRemoteEmailSender;
import com.galsie.gcs.microservicecommon.lib.email.data.discrete.EmailType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.microservicecommon.lib.sms.GCSRemoteSMSSender;
import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSType;
import com.galsie.gcs.users.configuration.security.contexthelper.UserGalSecurityContextHelper;
import com.galsie.gcs.users.data.discrete.verification.OTPVerificationErrorType;
import com.galsie.gcs.users.data.discrete.verification.OTPVerificationRequestErrorType;
import com.galsie.gcs.users.data.discrete.verification.OTPVerificationType;
import com.galsie.gcs.users.data.dto.verification.request.perform.PerformOTPVerificationRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.resend.ResendOTPVerificationCodeRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.session.EmailOTPVerificationSessionRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.session.OTPVerificationSessionRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.session.PhoneOTPVerificationSessionRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.session.UserAccountOTPVerificationSessionRequestDTO;
import com.galsie.gcs.users.data.dto.verification.response.OTPVerificationSessionResponseDTO;
import com.galsie.gcs.users.data.dto.verification.response.PerformOTPVerificationResponseDTO;
import com.galsie.gcs.users.data.dto.verification.response.ResendOTPVerificationResponseDTO;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.data.entity.verification.OTPVerificationEntity;
import com.galsie.gcs.users.data.entity.verification.OTPVerificationSessionEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.EmailOTPVerificationEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.PhoneOTPVerificationEntity;
import com.galsie.gcs.users.data.entity.verification.otpverification.AccountOTPVerificationEntity;
import com.galsie.gcs.users.repository.verification.OTPVerificationEntityRepository;
import com.galsie.gcs.users.repository.verification.OTPVerificationSessionEntityRepository;
import com.galsie.gcs.users.repository.verification.otpverification.EmailOTPVerificationEntityRepository;
import com.galsie.gcs.users.repository.verification.otpverification.PhoneOTPVerificationEntityRepository;
import com.galsie.gcs.users.repository.verification.otpverification.UserAccountOTPVerificationEntityRepository;
import com.galsie.lib.utils.crypto.coder.CodingAlgorithm;
import com.galsie.lib.utils.crypto.hasher.Hasher;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
import com.galsie.lib.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class OTPVerificationService {


    /*
        ========================
        REQUEST OTP VERIFICATION
        ========================
     */

    /*
    Repositories for OTPVerificationEntity and its subclasses
     */
    @Autowired
    OTPVerificationEntityRepository otpVerificationEntityRepository;

    @Autowired
    EmailOTPVerificationEntityRepository emailOTPVerificationEntityRepository;

    @Autowired
    PhoneOTPVerificationEntityRepository phoneOTPVerificationEntityRepository;

    @Autowired
    UserAccountOTPVerificationEntityRepository userAccountOTPVerificationEntityRepository;
    /*
    Repository for OTP Verification Session Entity
     */
    @Autowired
    OTPVerificationSessionEntityRepository otpVerificationSessionEntityRepository;

    @Autowired
    GCSRemoteEmailSender gcsRemoteEmailSender;

    @Autowired
    GCSRemoteSMSSender gcsRemoteSMSSender;

    @Autowired
    UserGalSecurityContextHelper securityContextHelper;

    public GCSResponse<OTPVerificationSessionResponseDTO> requestOTPVerification(OTPVerificationSessionRequestDTO requestOTPVerificationDTO) {
        try {
            if (requestOTPVerificationDTO instanceof EmailOTPVerificationSessionRequestDTO requestEmailOTPVerificationDTO) {
                return gcsInternalRequestOTPVerificationByEmail(requestEmailOTPVerificationDTO);
            } else if (requestOTPVerificationDTO instanceof PhoneOTPVerificationSessionRequestDTO requestPhoneOTPVerificationDTO) {
                return gcsInternalRequestOTPVerificationByPhone(requestPhoneOTPVerificationDTO);
            } else if (requestOTPVerificationDTO instanceof UserAccountOTPVerificationSessionRequestDTO requestOTPVerificationForAuthenticatedUserDTO) {
                return gcsInternalRequestOTPVerificationForAuthenticatedUser(requestOTPVerificationForAuthenticatedUserDTO);
            }
        }catch (GCSResponseException exception){
            return exception.getGcsResponse(OTPVerificationSessionResponseDTO.class);
        }
        return GCSResponse.errorResponseWithMessage(GCSResponseErrorType.NOT_SUPPORTED, "DTO not supported for this request");
    }

    /*

     */
    @Transactional
    private GCSResponse<OTPVerificationSessionResponseDTO> gcsInternalRequestOTPVerificationByEmail(EmailOTPVerificationSessionRequestDTO requestEmailOTPVerificationDTO) {
        if (!requestEmailOTPVerificationDTO.isEmailValid()) {
            return OTPVerificationSessionResponseDTO.errorResponse(OTPVerificationRequestErrorType.INVALID_EMAIL);
        }
        String email = requestEmailOTPVerificationDTO.getEmail();
        var emailOTPVerificationEntity = emailOTPVerificationEntityRepository.findByEmail(email).orElse(null);
        if (emailOTPVerificationEntity == null) {
            emailOTPVerificationEntity = EmailOTPVerificationEntity.builder().email(email).build();
            GCSResponse.saveEntityThrows(emailOTPVerificationEntityRepository, emailOTPVerificationEntity);
        }
        var otp = gcsInternalGenerateOTP();
        var generateSessionResponse = gcsInternalGenerateOTPVerificationSessionEntityFor(otp, emailOTPVerificationEntity, LocalDateTime.now().plusMinutes(OTPVerificationSessionEntity.TOKEN_VALIDITY_MINUTES));
        if (generateSessionResponse.hasError()) {
            return GCSResponse.errorResponse(generateSessionResponse.getGcsError());
        }
        var otpVerificationSessionEntity = generateSessionResponse.getResponseData();
        /*
         SEND EMAIL, USE RABBITMQ
         */
        gcsInternalEmailVerification(email, otp);

        return OTPVerificationSessionResponseDTO.successResponse(otpVerificationSessionEntity.getVerificationToken());
    }

    /*

     */
    @Transactional
    private GCSResponse<OTPVerificationSessionResponseDTO> gcsInternalRequestOTPVerificationByPhone(PhoneOTPVerificationSessionRequestDTO requestPhoneOTPVerificationDTO) {
        if (!requestPhoneOTPVerificationDTO.isValid()) {
            return OTPVerificationSessionResponseDTO.errorResponse(OTPVerificationRequestErrorType.INVALID_PHONE);
        }
        short countryCode = requestPhoneOTPVerificationDTO.getCountryCode();
        String phoneNumber = requestPhoneOTPVerificationDTO.getPhoneNumber();
        var phoneOTPVerificationEntity = phoneOTPVerificationEntityRepository.findByCountryCodeAndPhoneNumber(countryCode, phoneNumber).orElse(null);
        if (phoneOTPVerificationEntity == null) {
            phoneOTPVerificationEntity = PhoneOTPVerificationEntity.builder().countryCode(countryCode).phoneNumber(phoneNumber).build();
            GCSResponse.saveEntityThrows(phoneOTPVerificationEntityRepository, phoneOTPVerificationEntity);
        }
        var otp = gcsInternalGenerateOTP();
        var generateSessionResponse = gcsInternalGenerateOTPVerificationSessionEntityFor(otp, phoneOTPVerificationEntity, LocalDateTime.now().plusMinutes(OTPVerificationSessionEntity.TOKEN_VALIDITY_MINUTES));
        if (generateSessionResponse.hasError()) {
            return GCSResponse.errorResponse(generateSessionResponse.getGcsError());
        }
        var otpVerificationSessionEntity = generateSessionResponse.getResponseData();
        gcsInternalSMSVerification(countryCode , phoneNumber, otp);
        return OTPVerificationSessionResponseDTO.successResponse(otpVerificationSessionEntity.getVerificationToken());
    }

    /*
    Expect
     */
    @Transactional
    public GCSResponse<OTPVerificationSessionResponseDTO> gcsInternalRequestOTPVerificationForAuthenticatedUser(UserAccountOTPVerificationSessionRequestDTO requestOTPVerificationForAuthenticatedUserDTO)throws GCSResponseException{
        var userContextInfo = securityContextHelper.getAuthenticatedUserInfo();
        return this.gcsInternalRequestOTPVerificationForUserAccount(userContextInfo.getUserAuthSessionEntity().getUser(), requestOTPVerificationForAuthenticatedUserDTO.getVerificationType());
    }

    @Transactional
    public GCSResponse<OTPVerificationSessionResponseDTO> gcsInternalRequestOTPVerificationForUserAccount(UserAccountEntity userAccountEntity, OTPVerificationType otpVerificationType) {
        if (otpVerificationType == OTPVerificationType.DEFAULT){
            otpVerificationType = userAccountEntity.getUserEmail() != null ? OTPVerificationType.EMAIL : OTPVerificationType.PHONE;
        }
        var userAccountOTPVerificationEntity = userAccountOTPVerificationEntityRepository.findByUser(userAccountEntity).orElse(null);
        if (userAccountOTPVerificationEntity == null) {
            userAccountOTPVerificationEntity = AccountOTPVerificationEntity.builder().user(userAccountEntity).build();
            GCSResponse.saveEntityThrows(userAccountOTPVerificationEntityRepository, userAccountOTPVerificationEntity);
        }
        var otp = gcsInternalGenerateOTP();
        var generateSessionResponse = gcsInternalGenerateOTPVerificationSessionEntityFor(otp, userAccountOTPVerificationEntity, LocalDateTime.now().plusMinutes(OTPVerificationSessionEntity.TOKEN_VALIDITY_MINUTES));
        if (generateSessionResponse.hasError()) {
            return GCSResponse.errorResponse(generateSessionResponse.getGcsError());
        }
        var otpVerificationSessionEntity = generateSessionResponse.getResponseData();
        if (otpVerificationType == OTPVerificationType.EMAIL){
            gcsInternalEmailVerification(userAccountEntity.getUserEmail().getEmail(), otp);
        }else{
            var phoneEntity = userAccountEntity.getUserPhone();
            if(phoneEntity == null){
                return OTPVerificationSessionResponseDTO.errorResponse(OTPVerificationRequestErrorType.VERIFICATION_TYPE_NOT_SET_UP);
            }
            gcsInternalSMSVerification(phoneEntity.getCountryCode(), phoneEntity.getPhoneNumber(), otp);
        }
        return OTPVerificationSessionResponseDTO.successResponse(otpVerificationSessionEntity.getVerificationToken());
    }

    @Transactional
    private GCSResponse<OTPVerificationSessionEntity> gcsInternalGenerateOTPVerificationSessionEntityFor(String otp, OTPVerificationEntity otpVerificationEntity, LocalDateTime validUntil) {
        var token = StringUtils.randomAlphanumeric(OTPVerificationSessionEntity.TOKEN_LENGTH);
        var otpVerificationSessionEntity = OTPVerificationSessionEntity.builder().otpVerificationEntity(otpVerificationEntity).hashedVerificationCode(gcsInternalHashOTPVerificationCode(otp)).verificationToken(token).validUntil(validUntil).build();
        return GCSResponse.saveEntityThrows(otpVerificationSessionEntityRepository, otpVerificationSessionEntity);
    }

    private String gcsInternalGenerateOTP(){
        short otp =  (short) (Math.random() * 9999);
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(otp));
        while (builder.toString().length() < 4){
            builder.insert(0, '0');
        }
        return builder.toString();
    }


    private String gcsInternalHashOTPVerificationCode(String otp){
        return Hasher.hashValue(otp, HashingAlgorithm.SHA256, CodingAlgorithm.BASE64);
    }

    /*
      ========================
      RESEND OTP VERIFICATION
      ========================
   */
    @Transactional
    public GCSResponse<ResendOTPVerificationResponseDTO> resendOTPVerificationCode(ResendOTPVerificationCodeRequestDTO resendOTPVerificationRequestDTO) {
        var otpVerificationSessionEntityOpt = otpVerificationSessionEntityRepository.findByVerificationToken(resendOTPVerificationRequestDTO.getOtpVerificationToken());
        if (otpVerificationSessionEntityOpt.isEmpty()) {
            return ResendOTPVerificationResponseDTO.responseError(OTPVerificationErrorType.INVALID_TOKEN);
        }

        var otpVerificationSessionEntity = otpVerificationSessionEntityOpt.get();
        if (!otpVerificationSessionEntity.isActive()) {
            return ResendOTPVerificationResponseDTO.responseError(OTPVerificationErrorType.INACTIVE_TOKEN);
        }

        if (otpVerificationSessionEntity.isVerified()){
            return ResendOTPVerificationResponseDTO.responseError(OTPVerificationErrorType.ALREADY_VALIDATED);
        }
        var otp = gcsInternalGenerateOTP();
        otpVerificationSessionEntity.setHashedVerificationCode(gcsInternalHashOTPVerificationCode(otp));
        GCSResponse.saveEntityThrows(otpVerificationSessionEntityRepository, otpVerificationSessionEntity);

        var otpVerificationEntity = otpVerificationSessionEntity.getOtpVerificationEntity();
        if (otpVerificationEntity instanceof EmailOTPVerificationEntity emailOTPVerificationEntity){
            gcsInternalEmailVerification(emailOTPVerificationEntity.getEmail(), otp);
        }else if (otpVerificationEntity instanceof PhoneOTPVerificationEntity phoneOTPVerificationEntity){
            gcsInternalSMSVerification(phoneOTPVerificationEntity.getCountryCode() ,  phoneOTPVerificationEntity.getPhoneNumber(), otp);
        }else if (otpVerificationEntity instanceof AccountOTPVerificationEntity accountOTPVerificationEntity){
            gcsInternalUserAccountVerification(accountOTPVerificationEntity, otp, resendOTPVerificationRequestDTO.getOtpVerificationType());
        }else{
            return GCSResponse.errorResponse(GCSResponseErrorType.NOT_SUPPORTED);
        }
        return ResendOTPVerificationResponseDTO.responseSuccess();
    }
    /*
        ========================
        PERFORM OTP VERIFICATION
        ========================
     */

    public GCSResponse<PerformOTPVerificationResponseDTO> performOTPVerification(PerformOTPVerificationRequestDTO performOTPVerificationRequestDTO){
        try{
            return gcsInternalPerformOTPVerification(performOTPVerificationRequestDTO);
        }catch (GCSResponseException exception){
            return exception.getGcsResponse(PerformOTPVerificationResponseDTO.class);
        }
    }
    @Transactional
    public GCSResponse<PerformOTPVerificationResponseDTO> gcsInternalPerformOTPVerification(PerformOTPVerificationRequestDTO performOTPVerificationRequestDTO) {
        if (!performOTPVerificationRequestDTO.isOTPVerificationTokenValid()) {
            return PerformOTPVerificationResponseDTO.responseError(OTPVerificationErrorType.INVALID_TOKEN);
        }
        if (!performOTPVerificationRequestDTO.isHashedOTPValid()) {
            return PerformOTPVerificationResponseDTO.responseSuccess(false);
        }

        var otpVerificationSessionEntityOpt = otpVerificationSessionEntityRepository.findByVerificationToken(performOTPVerificationRequestDTO.getOtpVerificationToken());
        if (otpVerificationSessionEntityOpt.isEmpty()) {
            return PerformOTPVerificationResponseDTO.responseError(OTPVerificationErrorType.INVALID_TOKEN);
        }

        var otpVerificationSessionEntity = otpVerificationSessionEntityOpt.get();
        if (!otpVerificationSessionEntity.isActive()) {
            return PerformOTPVerificationResponseDTO.responseError(OTPVerificationErrorType.INACTIVE_TOKEN);
        }

        if (otpVerificationSessionEntity.isVerified()){
            return PerformOTPVerificationResponseDTO.responseError(OTPVerificationErrorType.ALREADY_VALIDATED);
        }
        if (otpVerificationSessionEntity.getHashedVerificationCode().equals(performOTPVerificationRequestDTO.getHashedOTP())) {
            otpVerificationSessionEntity.setVerified(true);
            GCSResponse.saveEntityThrows(otpVerificationSessionEntityRepository, otpVerificationSessionEntity);
        }
        return PerformOTPVerificationResponseDTO.responseSuccess(otpVerificationSessionEntity.isVerified());
    }

    public void gcsInternalEmailVerification(String email, String otp){
        var map = new HashMap<String, String>();
        map.put("verification_code", otp);
        gcsRemoteEmailSender.sendEmail(EmailType.USER_EMAIL_VERIFICATION, email, map);
    }

    public void gcsInternalSMSVerification(short countryCode, String phoneNumber, String otp){
        var map = new HashMap<String, String>();
        map.put("otp-code",otp);
        gcsRemoteSMSSender.sendSms(SMSType.OTP_SMS_VERIFICATION, countryCode, phoneNumber, map);
    }

    public void gcsInternalUserAccountVerification(AccountOTPVerificationEntity accountOTPVerificationEntity, String otp, OTPVerificationType otpVerificationType){
        if (otpVerificationType == null || otpVerificationType.equals(OTPVerificationType.DEFAULT)){
            otpVerificationType = accountOTPVerificationEntity.getUser().getUserEmail() != null ? OTPVerificationType.EMAIL : OTPVerificationType.PHONE;
        }
        if(otpVerificationType.equals(OTPVerificationType.EMAIL)){
            gcsInternalEmailVerification(accountOTPVerificationEntity.getUser().getUserEmail().getEmail(), otp);
        }else{
            gcsInternalSMSVerification(accountOTPVerificationEntity.getUser().getUserPhone().getCountryCode()
                    , accountOTPVerificationEntity.getUser().getUserPhone().getPhoneNumber(), otp);
        }
    }

    /*
    Check if validated
     */

    public boolean gcsInternalIsOTPVerificationSessionVerifiedForOTPVerificationToken(String otpVerificationToken){
        var otpVerificationSessionEntityOpt = otpVerificationSessionEntityRepository.findByVerificationToken(otpVerificationToken);
        return otpVerificationSessionEntityOpt.map(OTPVerificationSessionEntity::isVerified).orElse(false);
    }

}
