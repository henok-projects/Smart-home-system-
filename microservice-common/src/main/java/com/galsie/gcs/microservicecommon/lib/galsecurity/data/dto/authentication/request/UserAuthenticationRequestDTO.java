package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthenticationRequestDTO extends TokenAuthenticationRequestDTO {

    public UserAuthenticationRequestDTO(String token){
        super(token);
    }
    public UserAuthenticationRequestDTO(){
        super();
    }
}
