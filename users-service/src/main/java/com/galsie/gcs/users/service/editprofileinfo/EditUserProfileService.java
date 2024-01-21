package com.galsie.gcs.users.service.editprofileinfo;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.users.configuration.security.contexthelper.UserGalSecurityContextHelper;
import com.galsie.gcs.users.data.discrete.UserGender;
import com.galsie.gcs.users.data.discrete.editprofileinfo.*;
import com.galsie.gcs.users.data.dto.editprofileinfo.dateofbirth.request.EditUserBirthdateRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.dateofbirth.response.EditUserBirthdateResponseDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.email.request.EditUserEmailRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.email.response.EditUserEmailResponseDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.fullname.request.EditUserFullNameRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.fullname.response.EditUserFullNameResponseDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.gender.request.EditUserGenderRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.gender.response.EditUserGenderResponseDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.password.request.EditUserPasswordRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.password.response.EditUserPasswordResponseDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.phone.request.EditUserPhoneRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.phone.response.EditUserPhoneResponseDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.username.request.EditUserUsernameRequestDTO;
import com.galsie.gcs.users.data.dto.editprofileinfo.username.response.EditUserUsernameResponseDTO;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.data.entity.UserEmailEntity;
import com.galsie.gcs.users.data.entity.UserInfoEntity;
import com.galsie.gcs.users.data.entity.UserPhoneEntity;
import com.galsie.gcs.users.data.entity.security.GalUserSecurityEntity;
import com.galsie.gcs.users.repository.UserAccountRepository;
import com.galsie.gcs.users.repository.UserEmailRepository;
import com.galsie.gcs.users.repository.UserInfoRepository;
import com.galsie.gcs.users.repository.UserPhoneRepository;
import com.galsie.gcs.users.repository.security.GalUserSecurityRepository;
import com.galsie.gcs.users.repository.security.UserAccountSecurityPreferencesRepository;
import com.galsie.gcs.users.repository.verification.OTPVerificationEntityRepository;
import com.galsie.gcs.users.repository.verification.OTPVerificationSessionEntityRepository;
import com.galsie.gcs.users.repository.verification.otpverification.EmailOTPVerificationEntityRepository;
import com.galsie.gcs.users.repository.verification.otpverification.PhoneOTPVerificationEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
public class EditUserProfileService {

    @Autowired
    UserGalSecurityContextHelper userGalSecurityContextHelper;

    @Autowired
    EmailOTPVerificationEntityRepository emailOTPVerificationEntityRepository;

    @Autowired
    PhoneOTPVerificationEntityRepository phoneOTPVerificationEntityRepository;
    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    OTPVerificationSessionEntityRepository otpVerificationSessionEntityRepository;

    @Autowired
    OTPVerificationEntityRepository otpVerificationEntityRepository;
    @Autowired
    GalUserSecurityRepository galUserSecurityRepository;

    @Autowired
    UserEmailRepository userEmailRepository;

    @Autowired
    UserPhoneRepository userPhoneRepository;

    @Autowired
    UserInfoRepository userInfoRepository;
    @Autowired
    UserAccountSecurityPreferencesRepository userAccountSecurityPreferencesRepository;

    public GCSResponse<EditUserEmailResponseDTO> requestEditEmail(EditUserEmailRequestDTO request) throws GCSResponseException {
        try {
            return gcsInternalRequestEditEmail(request);
        } catch (GCSResponseException e){
            return e.getGcsResponse(EditUserEmailResponseDTO.class);
        }

    }

    @Transactional
    GCSResponse<EditUserEmailResponseDTO> gcsInternalRequestEditEmail(EditUserEmailRequestDTO request) throws GCSResponseException {
        var errorType1 = request.validateEditingVerificationToken();
        if(errorType1.isEmpty()){
            errorType1 = validateEditingToken(request.getEditingVerificationToken());
        }
        var errorType2 = request.validateEmailVerificationToken();

        if (errorType1.isPresent() || errorType2.isPresent()) {
            return EditUserEmailResponseDTO.responseError(errorType1.orElse(null), errorType2.orElse(null));
        }

        // Extract User ID from Auth Session
        var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();

        // Retrieve user from the repository
        Optional<UserAccountEntity> userOptional = userAccountRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return EditUserEmailResponseDTO.responseError( EditUserInfoVerificationErrorType.UNAUTHORIZED_USER_EXCEPTION, null);
        }
        var user = userOptional.get();
        // Extract new email from the new email verification token
        var newEmailOTPVerificationSessionEntity = otpVerificationSessionEntityRepository.findByVerificationToken(request.getNewEmailVerificationToken());
        if(newEmailOTPVerificationSessionEntity.isEmpty()){
            return EditUserEmailResponseDTO.responseError(null, EditUserEmailResponseErrorType.INVALID_NEW_EMAIL_OTP_TOKEN);
        }
        if (!newEmailOTPVerificationSessionEntity.get().isActive()) {
            return EditUserEmailResponseDTO.responseError(null, EditUserEmailResponseErrorType.INACTIVE_NEW_EMAIL_OTP_TOKEN);
        }

        var verEntityId = newEmailOTPVerificationSessionEntity.get().getOtpVerificationEntity().getId();
        var emailOTPVerificationEntity = emailOTPVerificationEntityRepository.findById(verEntityId);
        if(emailOTPVerificationEntity.isEmpty()){
            return EditUserEmailResponseDTO.responseError(null, EditUserEmailResponseErrorType.INVALID_VERIFICATION_ENTITY_ERROR);
        }
        var newEmail = emailOTPVerificationEntity.get().getEmail();

        if(!newEmailOTPVerificationSessionEntity.get().isVerified()){
            return EditUserEmailResponseDTO.responseError(null, EditUserEmailResponseErrorType.UNVERIFIED_NEW_EMAIL_OTP_TOKEN);
        }

        // Check if the new EMail already exists
        if(isEmailAssociatedWithOtherAccount(newEmail)) {
            return EditUserEmailResponseDTO.responseError(null, EditUserEmailResponseErrorType.NEW_EMAIL_IS_ASSOCIATED_WITH_ANOTHER_USER);
        }

        // Both verification tokens are valid, update the email
        boolean isEmailUpdated = updateEmail(user.getId(), newEmail);
        if (isEmailUpdated) {
            return EditUserEmailResponseDTO.responseSuccess();
        } else {
            //error type need to be checked
            return EditUserEmailResponseDTO.responseError(null,EditUserEmailResponseErrorType.INVALID_NEW_EMAIL_OTP_TOKEN);
        }
        // Either old or new email verification failed
    }
    public GCSResponse<EditUserPhoneResponseDTO> requestEditPhone(EditUserPhoneRequestDTO request) throws GCSResponseException {
        try {
            return gcsInternalRequestEditPhone(request);
        } catch (GCSResponseException e){
            return e.getGcsResponse(EditUserPhoneResponseDTO.class);
        }

    }

    @Transactional
    GCSResponse<EditUserPhoneResponseDTO> gcsInternalRequestEditPhone(EditUserPhoneRequestDTO request) throws GCSResponseException {
        var errorType1 = request.validateToken();
        if(errorType1.isEmpty()){
            errorType1 = validateEditingToken(request.getEditingVerificationToken());
        }
        var errorType2= request.validatePhone();

        if (errorType1.isPresent() || errorType2.isPresent()) {
            return EditUserPhoneResponseDTO.responseError(errorType1.orElse(null), errorType2.orElse(null));
        }
        // Extract User ID from Auth Session
        var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();

        // Retrieve user from the repository
        Optional<UserAccountEntity> userOptional = userAccountRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return EditUserPhoneResponseDTO.responseError(EditUserInfoVerificationErrorType.UNAUTHORIZED_USER_EXCEPTION, null);
        }

        var user = userOptional.get();

        // Extract new email from the new email verification token
        var newPhoneOTPVerificationEntity = otpVerificationSessionEntityRepository.findByVerificationToken(request.getNewPhoneVerificationToken());
        if(newPhoneOTPVerificationEntity.isEmpty()){
            return EditUserPhoneResponseDTO.responseError(null, EditUserPhoneResponseErrorType.INVALID_NEW_PHONE_OTP_TOKEN);
        }
        if (!newPhoneOTPVerificationEntity.get().isActive()) {
            return EditUserPhoneResponseDTO.responseError(null,EditUserPhoneResponseErrorType.INACTIVE_NEW_PHONE_OTP_TOKEN);
        }
        var verEntityId = newPhoneOTPVerificationEntity.get().getOtpVerificationEntity().getId();
        var phoneOTPVerificationEntityOpt= phoneOTPVerificationEntityRepository.findById(verEntityId);
        if(phoneOTPVerificationEntityOpt.isEmpty()){
            return EditUserPhoneResponseDTO.responseError(null, EditUserPhoneResponseErrorType.INVALID_VERIFICATION_ENTITY_ERROR);
        }


        var newPhone = phoneOTPVerificationEntityOpt.get().getPhoneNumber();
        var countryCode = phoneOTPVerificationEntityOpt.get().getCountryCode();

        if(!newPhoneOTPVerificationEntity.get().isVerified()){
            return EditUserPhoneResponseDTO.responseError(null, EditUserPhoneResponseErrorType.UNVERIFIED_NEW_PHONE_OTP_TOKEN);
        }
        // Check if the new phone already exists
        if(isPhoneAssociatedWithOtherAccount(countryCode,newPhone)) {
            return EditUserPhoneResponseDTO.responseError(null,EditUserPhoneResponseErrorType.NEW_PHONE_IS_ASSOCIATED_WITH_ANOTHER_USER);
        }

        // Both verification tokens are valid, update the email
        boolean isPhoneVerified = updatePhone(user.getId(), newPhone);
        if (isPhoneVerified) {
            return EditUserPhoneResponseDTO.responseSuccess();
        } else {
            return EditUserPhoneResponseDTO.responseError(null, EditUserPhoneResponseErrorType.INVALID_NEW_PHONE_OTP_TOKEN);
        }
    }

    public GCSResponse<EditUserUsernameResponseDTO> requestEditUsername(EditUserUsernameRequestDTO request) throws GCSResponseException {
        try {
            return gcsInternalRequestEditUsername(request);
        } catch (GCSResponseException e){
            return e.getGcsResponse(EditUserUsernameResponseDTO.class);
        }
    }

    @Transactional
    GCSResponse<EditUserUsernameResponseDTO> gcsInternalRequestEditUsername(EditUserUsernameRequestDTO request) throws GCSResponseException {
        var errorType1 = request.validateToken();
        if(errorType1.isEmpty()){
            errorType1 = validateEditingToken(request.getEditingVerificationToken());
        }
        var errorType2= request.validateUsername();
        if (errorType1.isPresent() || errorType2.isPresent()) {
            return EditUserUsernameResponseDTO.responseError(errorType1.orElse(null), errorType2.orElse(null));
        }
        // Extract User ID from Auth Session
        var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();

        // Retrieve user from the repository
        Optional<UserAccountEntity> userOptional = userAccountRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return EditUserUsernameResponseDTO.responseError(null, EditUserUsernameErrorType.INVALID_NEW_USERNAME_DATA);
        }

        UserAccountEntity user = userOptional.get();
        // Check if the new username already exists
        if (isUsernameExists(request.getUsername())) {
            return EditUserUsernameResponseDTO.responseError(null, EditUserUsernameErrorType.NEW_USERNAME_ALREADY_TAKEN);
        }
        // Update the username
        boolean isUsernameUpdated = updateUsername(user.getUsername(), request.getUsername());
        if (isUsernameUpdated) {
            return EditUserUsernameResponseDTO.responseSuccess();
        } else {
            return EditUserUsernameResponseDTO.responseError(null, EditUserUsernameErrorType.INVALID_NEW_USERNAME_DATA);
        }
    }

    public GCSResponse<EditUserFullNameResponseDTO> requestEditFullName(EditUserFullNameRequestDTO request) throws GCSResponseException {
        try {
            return gcsInternalRequestEditFullName(request);
        } catch (GCSResponseException e){
            return e.getGcsResponse(EditUserFullNameResponseDTO.class);
        }
    }

    @Transactional
    public GCSResponse<EditUserFullNameResponseDTO> gcsInternalRequestEditFullName(EditUserFullNameRequestDTO request) throws GCSResponseException {
        var errorType1 = request.validateEditingVerificationToken();
        if (errorType1.isEmpty()) {
            errorType1 = validateEditingToken(request.getEditingVerificationToken());
        }
        var errorType2 = request.validateFullName();
        if (errorType1.isPresent() || errorType2.isPresent()) {
            return EditUserFullNameResponseDTO.responseError(errorType1.orElse(null), errorType2.orElse(null));
        }

        // Extract User ID from Auth Session
        var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();

        // Retrieve user from the repository
        var userOptional = userAccountRepository.findById(userId);
        if(userOptional.isEmpty()){
            return EditUserFullNameResponseDTO.responseError(EditUserInfoVerificationErrorType.UNAUTHORIZED_USER_EXCEPTION, null);
        }
        UserAccountEntity user = userOptional.get();

        // Token is valid, update the full name
        boolean isFullNameUpdated = updateFullName(user.getId(), request);

        if (isFullNameUpdated) {
            return EditUserFullNameResponseDTO.responseSuccess();
        } else {
            return EditUserFullNameResponseDTO.responseError(null, EditUserFullNameResponseErrorType.INVALID_NEW_FIRST_NAME);
        }
    }

    public GCSResponse<EditUserBirthdateResponseDTO> requestEditBirthdate(EditUserBirthdateRequestDTO request) throws GCSResponseException {
        try {
            return gcsInternalRequestEditBirthdate(request);
        } catch (GCSResponseException e){
            return e.getGcsResponse(EditUserBirthdateResponseDTO.class);
        }
    }

    @Transactional
    public GCSResponse<EditUserBirthdateResponseDTO> gcsInternalRequestEditBirthdate(EditUserBirthdateRequestDTO request) throws GCSResponseException {
        var errorType1 = request.validateToken();
        if(errorType1.isEmpty()){
            errorType1 = validateEditingToken(request.getEditingVerificationToken());
        }
        var errorType2 = request.validateBirthdate();

        if (errorType1.isPresent() || errorType2.isPresent()) {
            return EditUserBirthdateResponseDTO.responseError(errorType1.orElse(null), errorType2.orElse(null));
        }

        // Extract User ID from Auth Session
        var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();

        // Retrieve user from the repository
        Optional<UserAccountEntity> userOptional = userAccountRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return EditUserBirthdateResponseDTO.responseError(EditUserInfoVerificationErrorType.UNAUTHORIZED_USER_EXCEPTION, null);
        }

        UserAccountEntity user = userOptional.get();
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(request.getBirthDate(), formatter);
            boolean isBirthdateUpdated = updateBirthDate(user.getId(), birthDate);

            if (isBirthdateUpdated) {
                return EditUserBirthdateResponseDTO.responseSuccess();
            } else {
                return EditUserBirthdateResponseDTO.responseError(null, EditUserBirthdateResponseErrorType.INVALID_NEW_BIRTHDATE_DATA);
            }
        }catch (DateTimeParseException ex){
            return EditUserBirthdateResponseDTO.responseError(null, EditUserBirthdateResponseErrorType.INVALID_NEW_BIRTHDATE_DATA);
        }
    }


    public GCSResponse<EditUserGenderResponseDTO> requestEditGender(EditUserGenderRequestDTO request) throws GCSResponseException {
        try {
            return gcsInternalRequestEditGender(request);
        } catch (GCSResponseException e){
            return e.getGcsResponse(EditUserGenderResponseDTO.class);
        }
    }

    @Transactional
    public GCSResponse<EditUserGenderResponseDTO> gcsInternalRequestEditGender(EditUserGenderRequestDTO request) throws GCSResponseException {
        var errorType1 = request.validateToken();
        if(errorType1.isEmpty()){
            errorType1 = validateEditingToken(request.getEditingVerificationToken());
        }
        var errorType2 = request.validateGender();

        if (errorType1.isPresent() || errorType2.isPresent()) {
            return EditUserGenderResponseDTO.responseError(errorType1.orElse(null), errorType2.orElse(null));
        }

        // Extract User ID from Auth Session
        var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();

        // Retrieve user from the repository
        Optional<UserAccountEntity> userOptional = userAccountRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return EditUserGenderResponseDTO.responseError(EditUserInfoVerificationErrorType.UNAUTHORIZED_USER_EXCEPTION,null);
        }

        UserAccountEntity user = userOptional.get();

        var newGender = UserGender.fromString(request.getGender());
        if(newGender.isEmpty()){
            return EditUserGenderResponseDTO.responseError(null, EditUserGenderResponseErrorType.INVALID_NEW_GENDER_DATA);
        }
        boolean isGenderUpdated = updateGender(user.getId(), newGender.get());
        if (isGenderUpdated) {
            return EditUserGenderResponseDTO.responseSuccess();
        } else {
            return EditUserGenderResponseDTO.responseError(null, EditUserGenderResponseErrorType.INVALID_NEW_GENDER_DATA);
        }
    }

    public GCSResponse<EditUserPasswordResponseDTO> requestEditPassword(EditUserPasswordRequestDTO request) throws GCSResponseException {
        try {
            return gcsInternalRequestEditPassword(request);
        } catch (GCSResponseException e){
            return e.getGcsResponse(EditUserPasswordResponseDTO.class);
        }
    }

    @Transactional
    public GCSResponse<EditUserPasswordResponseDTO> gcsInternalRequestEditPassword(EditUserPasswordRequestDTO request) throws GCSResponseException {
        var errorType1 = request.validateToken();
        if(errorType1.isEmpty()){
            errorType1 = validateEditingToken(request.getEditingVerificationToken());
        }
        var errorType2 = request.validatePassword();

        if (errorType1.isPresent() || errorType2.isPresent()) {
            return EditUserPasswordResponseDTO.responseError(errorType1.orElse(null), errorType2.orElse(null));
        }

        // Extract User ID from Auth Session
        var userId = userGalSecurityContextHelper.getAuthenticatedUserInfo().getUserAuthSessionEntity().getUser().getId();

        // Retrieve user from the repository
        Optional<UserAccountEntity> userOptional = userAccountRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return EditUserPasswordResponseDTO.responseError(null, EditUserPasswordResponseErrorType.INVALID_NEW_PASSWORD);
        }

        UserAccountEntity user = userOptional.get();

        if (updatePassword(user.getId(), request.getHashedPwd())) {
            return EditUserPasswordResponseDTO.responseSuccess();
        } else {
            return EditUserPasswordResponseDTO.responseError(null, EditUserPasswordResponseErrorType.INVALID_NEW_PASSWORD);
        }
    }



    private boolean isUsernameExists(String username) {
        Optional<UserAccountEntity> userAccountEntity = userAccountRepository.findByUsername(username);
        return  userAccountEntity.isPresent();
    }

    private boolean verifyToken(String verificationToken) {
        var tokenEntityOptional = otpVerificationSessionEntityRepository.findByVerificationToken(verificationToken);
        return tokenEntityOptional.isPresent() && tokenEntityOptional.get().isVerified();
    }
    private boolean isEmailAssociatedWithOtherAccount(String newEmail) {
        Optional<UserEmailEntity> userWithSameEmail = userEmailRepository.findByEmail(newEmail);
        return userWithSameEmail.isPresent();
    }
    private boolean isPhoneAssociatedWithOtherAccount(short cCode,String newPhone) {
        Optional<UserPhoneEntity> user = userPhoneRepository.findByCountryCodeAndPhoneNumber(cCode,newPhone);
        return user.isPresent();
    }
    private boolean updateEmail(Long userId, String newEmail) {
        Optional<UserAccountEntity> userOptional = userAccountRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserEmailEntity userEmailEntity;
            if (userOptional.get().getUserEmail() != null) {
                userEmailEntity = userOptional.get().getUserEmail();
                userEmailEntity.setEmail(newEmail);
            } else {
                userEmailEntity = UserEmailEntity.builder().user(userOptional.get()).email(newEmail).build();
            }
            userEmailRepository.save(userEmailEntity);
            return true;
        }
        return false;
    }
    private boolean updatePhone(Long userId, String newPhone) {
        Optional<UserAccountEntity> userOptional = userAccountRepository.findById(userId);
        //if user have old phone number
        if (userOptional.isPresent()) {
            UserPhoneEntity userPhoneEntity;
            if(userOptional.get().getUserPhone() !=null){
                userPhoneEntity = userOptional.get().getUserPhone();
                userPhoneEntity.setPhoneNumber(newPhone);
            }else{
                userPhoneEntity = UserPhoneEntity.builder().user(userOptional.get()).phoneNumber(newPhone).build();
            }
            userPhoneRepository.save(userPhoneEntity);
            return true;
        }
        return false;
    }
    private boolean updateUsername(String oldUsername, String newUsername) {
        Optional<UserAccountEntity> userOptional = userAccountRepository.findByUsername(oldUsername);
        if (userOptional.isPresent()) {
            UserAccountEntity user = userOptional.get();
            user.setUsername(newUsername);
            userAccountRepository.save(user);
            return true;
        }
        return false;
    }
    private boolean updateFullName(long userId,EditUserFullNameRequestDTO fullNameRequestDTO) {
        // Implement the logic to update the full name in the database
        Optional<UserInfoEntity> userInfoEntity = userInfoRepository.findById(userId);
        if(userInfoEntity.isPresent()){
            UserInfoEntity userInfo =userInfoEntity.get();

            userInfo.setFirstName(fullNameRequestDTO.getFirstName());
            userInfo.setLastName(fullNameRequestDTO.getLastName());
            userInfo.setMiddleName(fullNameRequestDTO.getMiddleName());

            userInfoRepository.save(userInfo);
            return true;
        }
        return false;
    }
    private boolean updateBirthDate(long userId, LocalDate birthDate) {
        // Implement the logic to update the full name in the database
        Optional<UserInfoEntity> userInfoEntity = userInfoRepository.findById(userId);
        if(userInfoEntity.isPresent()){
            UserInfoEntity userInfo =userInfoEntity.get();
            userInfo.setBirthDate(birthDate);
            userInfoRepository.save(userInfo);
            return true;
        }
        return false;
    }
    private boolean updateGender(long userId, UserGender userGender) {
        // Implement the logic to update the full name in the database
        Optional<UserInfoEntity> userInfoEntity = userInfoRepository.findById(userId);
        if(userInfoEntity.isPresent()){
            UserInfoEntity userInfo =userInfoEntity.get();
            userInfo.setGender(userGender);
            userInfoRepository.save(userInfo);
            return true;
        }
        return false;
    }

    private boolean updatePassword(Long userId, String newPassword) {
        Optional<GalUserSecurityEntity> galUserSecurityEntity = galUserSecurityRepository.findById(userId);
        if(galUserSecurityEntity.isPresent()){
            GalUserSecurityEntity userSecurityEntity = galUserSecurityEntity.get();
            userSecurityEntity.setPassword(newPassword);
            galUserSecurityRepository.save(userSecurityEntity);
            return true;
        }
        return false;
    }

    private Optional<EditUserInfoVerificationErrorType> validateEditingToken(String verificationToken){
        var otpVerificationSessionEntityOpt = otpVerificationSessionEntityRepository.findByVerificationToken(verificationToken);
        if (otpVerificationSessionEntityOpt.isEmpty()){
            return Optional.of(EditUserInfoVerificationErrorType.INVALID_EDITING_VERIFICATION_TOKEN);
        }
        var otpVerificationSessionEntity = otpVerificationSessionEntityOpt.get();
        if (!otpVerificationSessionEntity.isActive()){
            return Optional.of(EditUserInfoVerificationErrorType.INACTIVE_EDITING_VERIFICATION_TOKEN);
        }

        if (!otpVerificationSessionEntity.isVerified()){
            return Optional.of(EditUserInfoVerificationErrorType.UNVERIFIED_EDITING_VERIFICATION_TOKEN);
        }
        return Optional.empty();
    }
}