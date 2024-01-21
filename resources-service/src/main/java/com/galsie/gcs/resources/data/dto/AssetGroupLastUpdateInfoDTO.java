package com.galsie.gcs.resources.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssetGroupLastUpdateInfoDTO {

    private LocalDateTime lastUpdate; // the last time the model was updated/changed in terms of its files
    private boolean isLastUpdateRequired; // is that change required

}
