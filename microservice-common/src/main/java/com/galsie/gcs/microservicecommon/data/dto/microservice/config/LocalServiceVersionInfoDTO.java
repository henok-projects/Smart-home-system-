package com.galsie.gcs.microservicecommon.data.dto.microservice.config;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LocalServiceVersionInfoDTO extends ServiceVersionInfoDTO{
    public LocalServiceVersionInfoDTO(String serviceName, String version, boolean isVersionRequired) {
        super(serviceName, version, isVersionRequired);
    }
}
