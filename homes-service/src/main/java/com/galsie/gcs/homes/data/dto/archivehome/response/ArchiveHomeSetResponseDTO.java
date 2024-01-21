package com.galsie.gcs.homes.data.dto.archivehome.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ArchiveHomeSetResponseDTO {

    @JsonProperty("archive_home_responses")
    @Nullable
    private List<ArchiveSingleHomeResponseDTO> homeArchiveResponses;


}
