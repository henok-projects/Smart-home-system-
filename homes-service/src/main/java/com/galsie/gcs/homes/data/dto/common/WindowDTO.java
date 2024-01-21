package com.galsie.gcs.homes.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.entity.home.area.HomeAreaEntity;
import com.galsie.gcs.homes.data.entity.home.windows.HomeWindowEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WindowDTO {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("name")
    @Nullable
    private String name;


    public static List<HomeWindowEntity> toWindowsEntityList(List<WindowDTO> windowDTOList) {
        return windowDTOList.stream()
                .map(windowDTO -> HomeWindowEntity.builder()
                        .name(windowDTO.getName())
                        .build())
                .collect(Collectors.toList());
    }


    public static List<WindowDTO> fromWindowEntity(HomeAreaEntity homeAreaEntity) {
        return homeAreaEntity.getWindows().stream()
                .map(homeWindowEntity -> WindowDTO.builder()
                        .id(homeWindowEntity.getId())
                        .name(homeWindowEntity.getName())
                        .build())
                .collect(Collectors.toList());
    }


    public static WindowDTO fromEntity(HomeWindowEntity homeAreaWindowEntity) {
        return WindowDTO.builder().id(homeAreaWindowEntity.getId())
                .name(homeAreaWindowEntity.getName())
                .build();
    }
}
