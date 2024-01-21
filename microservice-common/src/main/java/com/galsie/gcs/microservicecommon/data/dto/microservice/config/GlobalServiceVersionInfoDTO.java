package com.galsie.gcs.microservicecommon.data.dto.microservice.config;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GlobalServiceVersionInfoDTO extends ServiceVersionInfoDTO{
    public GlobalServiceVersionInfoDTO(String serviceName, String version, boolean isVersionRequired) {
        super(serviceName, version, isVersionRequired);
    }
}
