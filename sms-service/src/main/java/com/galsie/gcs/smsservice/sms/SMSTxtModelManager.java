package com.galsie.gcs.smsservice.sms;

import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSType;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * SMSTxtModelManager handles the caching and retrieval of text-based SMS models.
 * The class maintains an internal cache of @{@link SMSType} to {@link SMSTxtModel}for efficient access to SMS models. If the requested SMS model
 * is not found in the cache, the manager will attempt to load it from the appropriate text file based on
 * the specified SMSType.
 */
@Slf4j
public class SMSTxtModelManager {

    static final long CACHE_SIZE_LIMIT = 1000;
    static final long EXPIRE_AFTER_HOURS = 6;
    private final Cache<SMSType, SMSTxtModel> txtSmsModelCache = Caffeine.newBuilder().maximumSize(CACHE_SIZE_LIMIT).expireAfterAccess(EXPIRE_AFTER_HOURS, TimeUnit.HOURS).build();

    public Optional<SMSTxtModel> getSmsModelFor(SMSType smsType){
        var smsModel =txtSmsModelCache.getIfPresent(smsType);
        if(smsModel != null){
            return Optional.of(smsModel);
        }
        var pathOpt = getFilePathFor(smsType);
        if (pathOpt.isEmpty()){
            log.info("Failed to get the TXTSmsModel for the sms type " + smsType.name() + " reason: Sms model path is not set");
            return Optional.empty();
        }
        try {
            smsModel = SMSTxtModel.fromTxtFile(pathOpt.get());
        }catch (Exception ex){
            log.info("Failed to get the TXTSmsModel for the sms type " + smsType.name() + " reason: " + ex.getLocalizedMessage());
            return Optional.empty();
        }
        txtSmsModelCache.put(smsType, smsModel);
        return Optional.of(smsModel);
    }


    private static Optional<String> getFilePathFor(SMSType smsType){
        var path = switch (smsType){
            case OTP_SMS_VERIFICATION -> "otpVerificationSms.txt";
            case USER_SMS_VERIFICATION -> "verifyUserSms.txt";
        };
        return Optional.of(path);
    }

}
