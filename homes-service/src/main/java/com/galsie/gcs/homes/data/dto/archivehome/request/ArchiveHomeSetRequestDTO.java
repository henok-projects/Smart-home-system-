package com.galsie.gcs.homes.data.dto.archivehome.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.Set;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ArchiveHomeSetRequestDTO {

    @JsonProperty("home_ids")
    @NotNull
    private Set<Long> homeIds;

}
