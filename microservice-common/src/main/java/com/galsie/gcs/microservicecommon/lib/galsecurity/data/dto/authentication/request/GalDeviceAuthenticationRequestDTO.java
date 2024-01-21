package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GalDeviceAuthenticationRequestDTO extends TokenAuthenticationRequestDTO {

    public GalDeviceAuthenticationRequestDTO(String token){
        super(token);
    }

    public GalDeviceAuthenticationRequestDTO(){
        super();
    }

}
