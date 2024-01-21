package com.galsie.gcs.microservicecommon.data.dto.microservice.login.request;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.utils.crypto.coder.CodingAlgorithm;
import com.galsie.lib.utils.crypto.hasher.Hasher;
import com.galsie.lib.utils.crypto.hasher.HashingAlgorithm;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GCSMicroserviceLoginRequestDTO {
    @NotNull
    String serviceName;

    @NotNull
    String hashedPwd;

    public static GCSMicroserviceLoginRequestDTO fromRawPassword(String serviceName, String rawPassword){
        var hashedPwd = Hasher.hashValue(rawPassword, HashingAlgorithm.SHA256, CodingAlgorithm.BASE64);
        return new GCSMicroserviceLoginRequestDTO(serviceName, hashedPwd); // Same hashing method as in gcs-sentry
    }
}
