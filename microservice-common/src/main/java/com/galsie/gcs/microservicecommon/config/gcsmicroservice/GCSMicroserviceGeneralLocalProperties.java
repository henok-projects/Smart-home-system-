package com.galsie.gcs.microservicecommon.config.gcsmicroservice;


import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/*
GCSMicroserviceGeneralLocalProperties is used in MicroserviceConfigurationService to store the latest version
 -MicroserviceConfigurationService is used by MicroserviceVersioningBootstrapService to ensure the microservice is up-to-date
 */
@Component
@Getter
public class GCSMicroserviceGeneralLocalProperties {

    @Value("${galsie.microservice.name}")
    String microserviceName;

    // instanceUniqueId used to have queue names unique to every microservice
    @Value("${galsie.microservice.instance-unique-id}") // TODO: have this be generated and persisted locally
    String instanceUniqueId;

    @Value("${galsie.microservice.password:#{null}}") // defaults to null
    String password;

    @Value("${galsie.microservice.version}")
    String version; // FORMAT: 0.0.0

    @Value("${galsie.microservice.version-required}")
    boolean isVersionRequired;


    public Optional<GCSMicroservice> getGCSMicroservice() {
        for(GCSMicroservice gcsMicroservice : GCSMicroservice.values()){
            if(gcsMicroservice.getName().equals(microserviceName.toLowerCase())){
                return Optional.of(gcsMicroservice);
            }
        }
        return Optional.empty();
    }

}
