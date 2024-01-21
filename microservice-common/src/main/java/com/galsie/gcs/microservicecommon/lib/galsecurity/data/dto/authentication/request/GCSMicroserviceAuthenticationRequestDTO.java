package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GCSMicroserviceAuthenticationRequestDTO extends TokenAuthenticationRequestDTO{


    public GCSMicroserviceAuthenticationRequestDTO(String token){
        super(token);
    }

    public GCSMicroserviceAuthenticationRequestDTO(){
        super();
    }

}
