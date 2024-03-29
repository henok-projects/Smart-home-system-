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
public class BookAssetDTO extends MockProvidedAssetDTO {
    
    @NotNull
    private String assets_file_version;

    @NotNull
    private String title;
    
    @NotNull
    private String author;
    
    @NotNull
    private int year;
    
    @NotNull
    private String isbn;
    
    @NotNull
    private BookPublisherDTO publisher;

    @Override
    public boolean valid() {
        return true;
    }
}


