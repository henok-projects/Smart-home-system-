package com.galsie.gcs.microservicecommon.bootstrap;

/*
    Configuration for version and its requirement
 */

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceGeneralLocalProperties;
import com.galsie.gcs.microservicecommon.data.entity.MicroserviceConfigurationEntity;
import com.galsie.gcs.microservicecommon.repository.MicroserviceConfigurationRepository;
import com.galsie.gcs.microservicecommon.service.MicroserviceConfigurationService;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.lib.utils.BooleanUtils;
import com.galsie.lib.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.galsie.gcs.microservicecommon.data.entity.MicroserviceConfigurationEntity.*;


/**
    Checks that the microservice is the latest version by:
    - Checking latest version in database, if not found, set it to the version of this microservice.
    - If found, compare against this version. If theres a newer version, and its required, shutdown this service.

    NOTE: this was tested with test-service and a database and it worked. To test again, manually update version in database and start this service.
 */

@Service
public class MicroServiceVersionCheckerBootstrapService {
    static Logger logger = LogManager.getLogger();


    @Autowired
    MicroserviceConfigurationRepository configurationRepository;

    @Autowired
    MicroserviceConfigurationService microserviceConfigurationService;

    @Autowired
    GCSMicroserviceGeneralLocalProperties microServiceConfiguration;


    public void bootstrap() throws Exception {
        var serviceName = microServiceConfiguration.getMicroserviceName();
        var configVersion = microServiceConfiguration.getVersion();
        var configVersionRequired = microServiceConfiguration.isVersionRequired();

        var storedVersionEnt = configurationRepository.findByMicroserviceAndConfigKey(serviceName, VERSION_KEY).orElse(null);
        if (storedVersionEnt == null){
            bootstrapAsNew();
            return;
        }
        // should be found, if not, throw
        var storedIsVersionRequiredEnt = configurationRepository.findByMicroserviceAndConfigKey(serviceName, VERSION_REQUIRED_KEY).orElseThrow();
        var storedVersion = storedVersionEnt.getConfigValue();
        var versionComparsion = StringUtils.compareVersions(storedVersion, configVersion);
        // If the versions are equal
        if (versionComparsion == 0){
            return;
        }
        // if storedVersion < this version, update version in database
        if (versionComparsion < 0){
            storedVersionEnt.setConfigValue(configVersion);
            storedIsVersionRequiredEnt.setConfigValue(String.valueOf(configVersionRequired));
            if (GCSResponse.saveEntities(configurationRepository, storedVersionEnt, storedIsVersionRequiredEnt).hasError()) {
                throw new Exception("Failed to bootstrap: Microservice has a newer version but it couldn't be saved");
            }
            return;
        }
        // If storedVersion is > this version
        // if the version is required, fail
        var isVersionReq = BooleanUtils.parseBoolean(storedIsVersionRequiredEnt.getConfigValue());
        if (isVersionReq.isEmpty()){
            throw new Exception("Failed to bootstrap: Couldn't parse isVersionReq boolean from string '" + storedIsVersionRequiredEnt.getConfigValue() + "'");
        }
        if (isVersionReq.get()) {
            throw new Exception("Failed to bootstrap: storedVersion is " + storedVersion + " > this version " + configVersion + ". Update the service");
        }
        // if not required, warn
        logger.warn("Warning while bootsrap configuration: storedVersion is " + storedVersion + " > this version " + configVersion + ". But the update is not required.");
    }

    private void bootstrapAsNew() throws Exception{
        final var config = microServiceConfiguration;
        var insertion =
                GCSResponse.saveEntities(configurationRepository,
                        new MicroserviceConfigurationEntity(null, config.getMicroserviceName(), VERSION_REQUIRED_KEY, String.valueOf(config.isVersionRequired())),
                        new MicroserviceConfigurationEntity(null, config.getMicroserviceName(), VERSION_KEY, String.valueOf(config.getVersion())));
        if (insertion.hasError()) {
            throw new Exception("Failed to bootstrap: Failed to save LatestMicroserviceConfigurationEntities reason: " + insertion.getGcsError().getErrorMsg());
        }
    }


}
