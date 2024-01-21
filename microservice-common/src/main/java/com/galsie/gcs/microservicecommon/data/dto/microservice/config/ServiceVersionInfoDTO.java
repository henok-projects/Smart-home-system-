package com.galsie.gcs.microservicecommon.data.dto.microservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@GalDTO
public abstract
class ServiceVersionInfoDTO {
    String serviceName;


    public ServiceVersionInfoDTO(String serviceName, String version, boolean isVersionRequired) {
    }


}
