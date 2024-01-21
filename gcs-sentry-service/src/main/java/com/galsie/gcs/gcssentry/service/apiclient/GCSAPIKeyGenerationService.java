package com.galsie.gcs.gcssentry.service.apiclient;

import com.galsie.gcs.gcssentry.data.entity.apiclient.APIClientKeyEntity;
import com.galsie.gcs.gcssentry.repository.apiclient.APIClientKeyEntityRepository;
import com.galsie.gcs.gcssentry.data.dto.generate.request.GCSApiKeyGenerateRequest;
import com.galsie.gcs.gcssentry.data.dto.generate.response.GCSAPIKeyGenerateResponse;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.entity.GalSecurityCommonSessionEntity;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.lib.utils.crypto.coder.CodingAlgorithm;
import com.galsie.lib.utils.crypto.hasher.Hasher;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class GCSAPIKeyGenerationService {

    @Autowired
    APIClientKeyEntityRepository apiClientKeyEntityRepo;

    public GCSResponse<GCSAPIKeyGenerateResponse> generateAPIClientKey(GCSApiKeyGenerateRequest gcsApiKeyGenerateRequest){
        try{
            return gcsInternalGenerateAPIClientKey(gcsApiKeyGenerateRequest);
        }catch (GCSResponseException e){
            return e.getGcsResponse(GCSAPIKeyGenerateResponse.class);
        }
    }

    @Transactional
    public GCSResponse<GCSAPIKeyGenerateResponse> gcsInternalGenerateAPIClientKey(GCSApiKeyGenerateRequest gcsApiKeyGenerateRequest){
        var deviceTypeAndName = gcsApiKeyGenerateRequest.getDeviceType() + ":" + gcsApiKeyGenerateRequest.getDeviceName()
                + ":" + LocalDateTime.now();
        var apiKey = Hasher.hashValue(deviceTypeAndName, HashingAlgorithm.SHA256, CodingAlgorithm.BASE64);
        var apiClientKeyEntity = APIClientKeyEntity.builder().apiKey(apiKey).expired(false)
                .validUntil(LocalDateTime.now().plusHours(GalSecurityCommonSessionEntity.SESSION_VALIDITY_HOURS)).lastAccess(LocalDateTime.now()).build();
        GCSResponse.saveEntityThrows(apiClientKeyEntityRepo, apiClientKeyEntity);
        return GCSAPIKeyGenerateResponse.responseSuccess(apiKey, gcsApiKeyGenerateRequest.getDeviceName());
    }

}
