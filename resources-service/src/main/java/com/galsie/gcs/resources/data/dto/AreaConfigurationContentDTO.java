package com.galsie.gcs.resources.data.dto;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@GalDTO
public class AreaConfigurationContentDTO {
    // TODO: remove and use implementation of asset providers in resources
    private double assetsFileVersion;
    private Map<String, String> initialsColors;

}
