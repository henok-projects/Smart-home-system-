package com.galsie.gcs.microservicecommon.service;

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceGeneralLocalProperties;
import com.galsie.gcs.microservicecommon.data.dto.microservice.config.GlobalServiceVersionInfoDTO;
import com.galsie.gcs.microservicecommon.data.dto.microservice.config.LocalServiceVersionInfoDTO;
import com.galsie.gcs.microservicecommon.repository.MicroserviceConfigurationRepository;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseErrorDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.lib.utils.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.galsie.gcs.microservicecommon.data.entity.MicroserviceConfigurationEntity.VERSION_KEY;
import static com.galsie.gcs.microservicecommon.data.entity.MicroserviceConfigurationEntity.VERSION_REQUIRED_KEY;

@Service
public class MicroserviceConfigurationService {

    @Autowired
    MicroserviceConfigurationRepository microserviceConfigurationRepository;

    // the repository above is populated based on the config below
    @Autowired
    GCSMicroserviceGeneralLocalProperties microServiceVersioningConfiguration;

    /**
     * Gets the service version information from GCSMicroserviceGeneralLocalProperties which is the local service versioning
     * found in application.yml
     * @return
     */
    public GCSResponse<?> getLocalServiceVersionInfo(){
        var serviceName = microServiceVersioningConfiguration.getMicroserviceName();
        var localVersionReq = microServiceVersioningConfiguration.isVersionRequired();
        var localVersion = microServiceVersioningConfiguration.getVersion();
        return GCSResponse.response(new LocalServiceVersionInfoDTO(serviceName, localVersion, localVersionReq));
    }

    /**
     * Gets the latest service version information from MicroserviceConfigurationRepository.
     *
     * Noting that: the latest version in the repository is updated when a microservice starts and it has a greater local version than that in the repo.
     * @return
     */
    public GCSResponse<GlobalServiceVersionInfoDTO> getGlobalServiceVersionInfo() {
        var serviceName = microServiceVersioningConfiguration.getMicroserviceName();
        var dbVersion = microserviceConfigurationRepository.findByMicroserviceAndConfigKey(serviceName, VERSION_KEY);
        if (dbVersion.isEmpty()) {
            return GCSResponse.errorResponse(GCSResponseErrorDTO.ofMessage(GCSResponseErrorType.ENTITY_NOT_FOUND, "Failed to find configuration entity with key: " + VERSION_KEY + " and serviceName: " + serviceName));
        }
        var dbVersionReqOpt = microserviceConfigurationRepository.findByMicroserviceAndConfigKey(serviceName, VERSION_REQUIRED_KEY);
        if (dbVersionReqOpt.isEmpty()) {
            return GCSResponse.errorResponse(GCSResponseErrorDTO.ofMessage(GCSResponseErrorType.ENTITY_NOT_FOUND, "Failed to find configuration entity with key: " + VERSION_KEY + " and serviceName: " + serviceName));
        }
        var dbVersionReqStr = dbVersionReqOpt.get().getConfigValue();
        var dbVersionReq = BooleanUtils.parseBoolean(dbVersionReqStr);
        if (dbVersionReq.isEmpty()){
            return GCSResponse.errorResponse(GCSResponseErrorDTO.ofMessage(GCSResponseErrorType.PARSING_ERROR, "Failed to parse boolean from string '" + dbVersionReqStr + "'"));
        }
        return GCSResponse.response(new GlobalServiceVersionInfoDTO(serviceName, dbVersion.get().getConfigValue(), dbVersionReq.get()));
    }

}
