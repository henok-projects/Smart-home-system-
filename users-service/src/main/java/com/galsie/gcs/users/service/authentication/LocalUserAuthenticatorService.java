package com.galsie.gcs.users.service.authentication;


import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.ActiveSessionListDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastUpdateResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.CodableUserAuthSessionToken;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.UserAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.user.UserActiveSessionListRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.UserAuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.ActiveSessionDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.users.data.entity.security.UserAuthSessionEntity;
import com.galsie.gcs.users.repository.UserAccountRepository;
import com.galsie.gcs.users.repository.UserAuthSessionRepository;
import com.galsie.gcs.users.service.verification.OTPVerificationService;
import com.galsie.lib.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.*;


@Service
public class LocalUserAuthenticatorService extends UserAuthenticator {

    @Value("${galsie.thisservice.userauth.max-session-inactive-time-in-seconds}")
    Long maximumSessionInactiveTimeSeconds;

    @Autowired
    UserAuthSessionRepository userAuthSessionRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    OTPVerificationService otpVerificationService;

    @Override
    public GCSResponse<? extends AuthenticationResponseDTO> performAuthentication(AuthenticationRequestDTO authenticationRequestDTO) {
        try{
            return gcsInternalPerformAuthentication(authenticationRequestDTO);
        }catch (GCSResponseException ex){
            return ex.getGcsResponse(AuthenticationResponseDTO.class);
        }
    }

    @Transactional
    public GCSResponse<AuthenticationResponseDTO> gcsInternalPerformAuthentication(AuthenticationRequestDTO authenticationRequestDTO) {
        if (!(authenticationRequestDTO instanceof UserAuthenticationRequestDTO userAuthenticationRequestDTO)){
            return GCSResponse.errorResponseWithMessage(GCSResponseErrorType.MISMATCH_ERROR, "Mismatch between the expected DTO and the passed one.");
        }
         /*
           Get the session token, try decoding it. If that failed, return an error
         */
        var sessionToken = userAuthenticationRequestDTO.getToken();
        var userAccountAuthSessionTokenOpt = CodableUserAuthSessionToken.fromStringToken(sessionToken);
        if (userAccountAuthSessionTokenOpt.isEmpty()) {
            return GCSResponse.errorResponseWithMessage(GCSResponseErrorType.PARSING_ERROR, "Failed to decode token");
        }
        /*
        Get the user authentication token, from it get the user id so that we can check if the account is locked, etc.
         */
        var userAuthSessionToken = userAccountAuthSessionTokenOpt.get();
        long userId = userAuthSessionToken.getUserId();
        var userAccountEntityOpt = userAccountRepository.findById(userId);
        if (userAccountEntityOpt.isEmpty()){
            return AuthenticationResponseDTO.responseError(GCSResponseErrorType.USER_ACCOUNT_NOT_FOUND);
        }
        /*
        Check if the account is deleted or disabled
         */
        var userAccountEntity = userAccountEntityOpt.get();
        switch (userAccountEntity.getAccountStatus()){
            case DELETED:
                return AuthenticationResponseDTO.responseError(GCSResponseErrorType.USER_ACCOUNT_DELETED);
            case DISABLED:
                return AuthenticationResponseDTO.responseError(GCSResponseErrorType.USER_ACCOUNT_DISABLED);
        }

        /*
        Validate the token
         */
        var sessionEntityOpt = userAuthSessionRepository.findBySessionToken(userAuthenticationRequestDTO.getToken());
        if (sessionEntityOpt.isEmpty()){
             return AuthenticationResponseDTO.responseError(GCSResponseErrorType.USER_AUTH_TOKEN_INVALID);
        }
        var sessionEntity = sessionEntityOpt.get();
        if (sessionEntity.isExpired()){
            return AuthenticationResponseDTO.responseError(GCSResponseErrorType.USER_AUTH_SESSION_EXPIRED);
        }
        /*
        If the session entity ha a 2fa token that was not verified, return an error.
        - IF otpVerificationToken is valid, set it to null to avoid this operation next time
         */
        if (sessionEntity.hasTwoFAOTPVerificationToken()){
            var otpVerificationToken = sessionEntity.getTwoFAOTPVerificationToken();
            if (!otpVerificationService.gcsInternalIsOTPVerificationSessionVerifiedForOTPVerificationToken(otpVerificationToken)) {
                return AuthenticationResponseDTO.responseError(GCSResponseErrorType.USER_AUTH_TWO_FA_REQUIRED);
            }
            // in this case, otp 2fa verification token is valid, so set it to null to avoid this operation next time
            sessionEntity.setTwoFAOTPVerificationToken(null); // set it to null to avoid this operation next time. the session entity is saved below
        }
        /*
            Update the last access time, extend the validity by SESSION_VALIDITY_HOURS if the remaining time until expiration is less than 20
         */
        this.gcsInternalReceiveAuthSessionLastAccessUpdate(sessionEntity, LocalDateTime.now());
        /*
        Return a responseSuccess which indicates that authentication was successfull
         */
        return AuthenticationResponseDTO.responseSuccess();
    }
    /*
    Update last access times
     */

    /**
     Updates the last access time, extend the validity by SESSION_VALIDITY_HOURS if the remaining time until expiration is less than 20
     - DOES NOTHING if the session is expired
     */
    @Override
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> receiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO) {
        try{
            return gcsInternalReceiveAuthSessionLastAccessUpdate(authSessionLastAccessUpdateDTO);
        }catch (GCSResponseException ex){
            return ex.getGcsResponse(AuthSessionLastAccessUpdateResponseDTO.class);
        }
    }
    /**
     Updates the last access time, extend the validity by SESSION_VALIDITY_HOURS if the remaining time until expiration is less than 20
     */
    @Transactional
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> gcsInternalReceiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO){
        var sessionEntityOpt = userAuthSessionRepository.findBySessionToken(authSessionLastAccessUpdateDTO.getSessionToken());
        if (sessionEntityOpt.isEmpty()){
            return AuthSessionLastAccessUpdateResponseDTO.responseError(AuthSessionLastUpdateResponseErrorType.SESSION_NOT_FOUND);
        }
        var sessionEntity = sessionEntityOpt.get();
        return gcsInternalReceiveAuthSessionLastAccessUpdate(sessionEntity, authSessionLastAccessUpdateDTO.getLastAccess());
    }

    /**
     Updates the last access time, extend the validity by SESSION_VALIDITY_HOURS if the remaining time until expiration is less than 20
    */
    @Transactional
    public GCSResponse<AuthSessionLastAccessUpdateResponseDTO> gcsInternalReceiveAuthSessionLastAccessUpdate(UserAuthSessionEntity sessionEntity, LocalDateTime lastAccess){
        if (sessionEntity.isExpired()){
            return AuthSessionLastAccessUpdateResponseDTO.responseError(AuthSessionLastUpdateResponseErrorType.SESSION_IS_EXPIRED);
        }
        if (sessionEntity.getRemainingTimeUntilValidityEndsInMinutes() < 20 ) { // extend the session validity if its about to end
            sessionEntity.setValidUntil(sessionEntity.getValidUntil().plusHours(UserAuthSessionEntity.SESSION_VALIDITY_HOURS));
        }

        var currentLastAccess = sessionEntity.getLastAccess();
        if (DateUtils.secondsBetween(currentLastAccess, lastAccess) <= 0) {
            return AuthSessionLastAccessUpdateResponseDTO.responseSuccess(); // if the stored last access is newer than the given last access, do nothing
        }
        sessionEntity.setLastAccess(lastAccess);
        saveEntityThrows(userAuthSessionRepository, sessionEntity);
        return AuthSessionLastAccessUpdateResponseDTO.responseSuccess();
    }

    /*
    Get Active Sessions
     */

    public GCSResponse<ActiveSessionListDTO<Long>> getUserAccountActiveSessions(UserActiveSessionListRequestDTO userActiveSessionListRequestDTO){
        return response(gcsInternalGetUserAccountActiveSessionsDTOFor(userActiveSessionListRequestDTO.getUserId(), Optional.ofNullable(userActiveSessionListRequestDTO.getAccessedAfter())));
    }

    public ActiveSessionListDTO<Long> gcsInternalGetUserAccountActiveSessionsDTOFor(long userId, Optional<LocalDateTime> accessedAfter){
        var activeSessionDTOList =  gcsInternalGetUserAccountActiveSessionsFor(userId, accessedAfter).stream().map((a) -> new ActiveSessionDTO(a.getSessionToken(), a.isForceExpired(), a.getValidUntil())).collect(Collectors.toList());
        return new ActiveSessionListDTO<Long>(userId, activeSessionDTOList);
    }

    public List<UserAuthSessionEntity> gcsInternalGetUserAccountActiveSessionsFor(long userId, Optional<LocalDateTime> accessedAfter){
        if (accessedAfter.isEmpty()) {
            return userAuthSessionRepository.getAllActiveByUserId(userId);
        }
        return userAuthSessionRepository.getAllActiveByUserIdAccessedAfter(userId, accessedAfter.get());
    }




}
