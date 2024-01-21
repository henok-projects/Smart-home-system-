package com.galsie.gcs.users.controller.verification;


import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.users.data.dto.verification.request.perform.PerformOTPVerificationRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.resend.ResendOTPVerificationCodeRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.session.EmailOTPVerificationSessionRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.session.PhoneOTPVerificationSessionRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.session.UserAccountOTPVerificationSessionRequestDTO;
import com.galsie.gcs.users.data.dto.verification.request.twofa.UserAccount2FARequestDTO;
import com.galsie.gcs.users.service.verification.OTPVerificationService;
import com.galsie.gcs.users.service.verification.TwoFAVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("verification")
public class OTPVerificationController {

    @Autowired
    OTPVerificationService otpVerificationService;

    @Autowired
    TwoFAVerificationService twoFAVerificationService;

    @PostMapping("/requestOTPVerification/email")
    ResponseEntity<?> requestEmailOTPVerification(@RequestBody EmailOTPVerificationSessionRequestDTO requestOTPVerificationDTO){
        return otpVerificationService.requestOTPVerification(requestOTPVerificationDTO).toResponseEntity();
    }

    @PostMapping("/requestOTPVerification/phone")
    ResponseEntity<?> requestEmailOTPVerification(@RequestBody PhoneOTPVerificationSessionRequestDTO requestOTPVerificationDTO){
        return otpVerificationService.requestOTPVerification(requestOTPVerificationDTO).toResponseEntity();
    }

    @AuthenticatedGCSRequest(authSessionTypes ={GalSecurityAuthSessionType.USER})
    @PostMapping("/requestOTPVerification/account")
    ResponseEntity<?> requestEmailOTPVerification(@RequestBody UserAccountOTPVerificationSessionRequestDTO requestOTPVerificationDTO){
        return otpVerificationService.requestOTPVerification(requestOTPVerificationDTO).toResponseEntity();
    }

    @PostMapping("/performOTPVerification")
    ResponseEntity<?> performOTPVerification(@RequestBody PerformOTPVerificationRequestDTO performOTPVerificationRequestDTO){
        return otpVerificationService.performOTPVerification(performOTPVerificationRequestDTO).toResponseEntity();
    }

    @PostMapping("/resendOTPVerification")
    ResponseEntity<?> resendOTPVerification(@RequestBody ResendOTPVerificationCodeRequestDTO resendOTPVerificationCodeRequestDTO){
        return otpVerificationService.resendOTPVerificationCode(resendOTPVerificationCodeRequestDTO).toResponseEntity();
    }

    @AuthenticatedGCSRequest(authSessionTypes = GalSecurityAuthSessionType.USER)
    @PostMapping("/requestTwoFactorAuthentication")
    ResponseEntity<?> requestOTPVerification(@RequestBody UserAccount2FARequestDTO userAccount2FARequestDTO){
        return twoFAVerificationService.authenticatedRequestTwoFactorAuthentication(userAccount2FARequestDTO).toResponseEntity();
    }
}
