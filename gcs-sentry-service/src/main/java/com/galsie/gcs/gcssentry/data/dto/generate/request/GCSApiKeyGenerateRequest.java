package com.galsie.gcs.gcssentry.data.dto.generate.request;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
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
public class GCSApiKeyGenerateRequest {

    @NotNull
    private String deviceType;

    @NotNull
    private String deviceName;

}
