package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.langlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Language {

    private Long id;

    private String name;

    private String langCode;

    private String image;

    private String localName;

    private String translationKey;

}
