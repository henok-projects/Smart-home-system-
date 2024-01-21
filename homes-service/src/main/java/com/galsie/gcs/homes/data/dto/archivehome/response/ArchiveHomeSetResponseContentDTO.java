package com.galsie.gcs.homes.data.dto.archivehome.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;
import java.util.ArrayList;
import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ArchiveHomeSetResponseContentDTO {

    @JsonProperty("archive_home_responses")
    @Nullable
    private List<ArchiveSingleHomeResponseDTO> homeArchiveResponses;

    public static ArchiveHomeSetResponseContentDTO fromArchivedHomeEntities(List<HomeEntity> homeEntities) {

            List<ArchiveSingleHomeResponseDTO> homeArchives = new ArrayList<>();
            homeEntities.stream().forEach(homeEntity -> {
                var homeArchive = ArchiveSingleHomeResponseDTO.builder()
                        .homeId(homeEntity.getHomeId())
                        .build();
                homeArchives.add(homeArchive);
            });

            return ArchiveHomeSetResponseContentDTO.builder()
                    .homeArchiveResponses(homeArchives)
                    .build();
        }

}
