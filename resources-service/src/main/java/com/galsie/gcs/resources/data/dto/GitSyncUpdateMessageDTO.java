package com.galsie.gcs.resources.data.dto;

import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceStatus;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@GalDTO
@Builder
public class GitSyncUpdateMessageDTO {

    @NotNull
    private GCSResourceMicroserviceStatus status;

}
