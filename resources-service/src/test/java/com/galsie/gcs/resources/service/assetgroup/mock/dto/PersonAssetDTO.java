package com.galsie.gcs.resources.service.assetgroup.mock.dto;

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
public class PersonAssetDTO extends MockProvidedAssetDTO {

    @NotNull
    private Float assets_file_version;

    @NotNull
    private int id;
    
    @NotNull
    private String name;

    @NotNull
    private int age;
    
    @NotNull
    private String email;

    @Override
    public boolean valid() {
        return true;
    }
}
