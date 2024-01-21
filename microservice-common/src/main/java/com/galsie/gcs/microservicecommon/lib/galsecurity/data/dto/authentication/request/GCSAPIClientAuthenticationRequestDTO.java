package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;

@GalDTO
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GCSAPIClientAuthenticationRequestDTO implements AuthenticationRequestDTO{

    private String apiKey;

    private String apiClientDeviceName;

}
