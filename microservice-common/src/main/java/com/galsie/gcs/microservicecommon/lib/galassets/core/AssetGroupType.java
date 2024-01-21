package com.galsie.gcs.microservicecommon.lib.galassets.core;

import com.galsie.lib.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum AssetGroupType {

    BRAND_THEME("branding", new AssetDataType[]{AssetDataType.PNG, AssetDataType.GIF, AssetDataType.YML, AssetDataType.TTF}, AssetVersioningType.DIRECTORY_VERSIONED),
    COUNTRIES("countries", new AssetDataType[]{AssetDataType.XML}, AssetVersioningType.FILE_VERSIONED),
    DATA_MODEL("datamodel", new AssetDataType[]{AssetDataType.XML}, AssetVersioningType.FILE_VERSIONED),
    LANG("lang", new AssetDataType[]{AssetDataType.YML}, AssetVersioningType.FILE_VERSIONED),
    APP_CONTENT("app_content", new AssetDataType[]{AssetDataType.JSON}, AssetVersioningType.FILE_VERSIONED);


    private final String mainAssetPath;
    private final AssetDataType[] dataTypes;
    private final AssetVersioningType versioningType;

    public String getPathInResources(){
        return StringUtils.joinPaths(GALASSETS_PATH,  mainAssetPath);
    }

    public static final String GALASSETS_PATH = "./gcs_generated_resources/GalAssets";
}
