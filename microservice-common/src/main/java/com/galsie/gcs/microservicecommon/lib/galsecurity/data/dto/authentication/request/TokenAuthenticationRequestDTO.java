package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class TokenAuthenticationRequestDTO implements AuthenticationRequestDTO {

    @NotNull
    String token;

}
