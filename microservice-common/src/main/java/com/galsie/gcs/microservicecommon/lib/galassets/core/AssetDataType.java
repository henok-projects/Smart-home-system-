package com.galsie.gcs.microservicecommon.lib.galassets.core;

import lombok.Getter;


@Getter
public enum AssetDataType {

    XML("xml"), YML("yml"), PNG("png"), GIF("gif"), TTF("ttf"), JSON("json");

    private final String extensionName;

    private AssetDataType(String extName){
        this.extensionName = extName;
    }

}
