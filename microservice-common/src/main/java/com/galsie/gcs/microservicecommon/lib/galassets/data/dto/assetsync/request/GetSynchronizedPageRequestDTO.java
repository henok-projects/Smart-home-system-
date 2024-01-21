package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetSynchronizedPageRequestDTO {

    @NotNull
    @JsonProperty("sync_id")
    private Long syncId;

    @NotNull
    private Long page;
}
