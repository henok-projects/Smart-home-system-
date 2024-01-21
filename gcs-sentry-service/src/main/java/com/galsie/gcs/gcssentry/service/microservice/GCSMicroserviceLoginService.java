package com.galsie.gcs.gcssentry.service.microservice;

import com.galsie.gcs.gcssentry.data.entity.microservice.GCSMicroserviceSessionEntity;
import com.galsie.gcs.gcssentry.repository.microservice.GCSMicroserviceSecurityEntityRepository;
import com.galsie.gcs.gcssentry.repository.microservice.GCSMicroserviceSessionEntityRepository;
import com.galsie.gcs.microservicecommon.data.dto.microservice.login.request.GCSMicroserviceLoginRequestDTO;
import com.galsie.gcs.microservicecommon.data.dto.microservice.login.response.GCSMicroserviceLoginResponseDTO;
import com.galsie.gcs.microservicecommon.data.dto.microservice.login.response.GCSMicroserviceLoginResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.microservice.CodableGCSMicroserviceAuthSessionToken;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Slf4j
public class GCSMicroserviceLoginService {
    @Autowired
    GCSMicroserviceSecurityEntityRepository gcsMicroserviceSecurityEntityRepository;

    @Autowired
    GCSMicroserviceSessionEntityRepository gcsMicroserviceSessionEntityRepository;

    public GCSResponse<GCSMicroserviceLoginResponseDTO> gcsMicroserviceLogin(GCSMicroserviceLoginRequestDTO gcsMicroserviceLoginRequestDTO){
        try{
            return gcsInternalMicroserviceLogin(gcsMicroserviceLoginRequestDTO);
        }catch (GCSResponseException e){
            return e.getGcsResponse(GCSMicroserviceLoginResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public GCSResponse<GCSMicroserviceLoginResponseDTO> gcsInternalMicroserviceLogin(GCSMicroserviceLoginRequestDTO gcsMicroserviceLoginRequestDTO) {
        var securityEntityOpt = gcsMicroserviceSecurityEntityRepository.findAllByHashedPwd(gcsMicroserviceLoginRequestDTO.getHashedPwd()).stream().filter((securityEntity) -> !securityEntity.isDeleted()).findFirst();
        if (securityEntityOpt.isEmpty()) {
            return GCSMicroserviceLoginResponseDTO.responseError(GCSMicroserviceLoginResponseErrorType.INVALID_CREDENTIALS);
        }
        var securityEntity = securityEntityOpt.get(); // wouldn't be deleted since we filtered above
        var sessionEntity = GCSMicroserviceSessionEntity.builder().securityEntity(securityEntity).expired(false).validUntil(LocalDateTime.now().plusHours(GCSMicroserviceSessionEntity.SESSION_VALIDITY_HOURS)).build();
        GCSResponse.saveEntityThrows(gcsMicroserviceSessionEntityRepository, sessionEntity)
                .getResponseData();
        var serviceName = gcsMicroserviceLoginRequestDTO.getServiceName();
        var codableToken = new CodableGCSMicroserviceAuthSessionToken(sessionEntity.getId(), serviceName);
        sessionEntity.setSessionToken(codableToken.toStringToken());
        GCSResponse.saveEntityThrows(gcsMicroserviceSessionEntityRepository, sessionEntity)
                .getResponseData();
        return GCSMicroserviceLoginResponseDTO.responseSuccess(sessionEntity.getSessionToken());
    }

}
