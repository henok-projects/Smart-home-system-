package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.countrycodes;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CountryCodeDTO {

    private  String id;

    private String countryName;

    private String countryCode;

    private String image;

}
