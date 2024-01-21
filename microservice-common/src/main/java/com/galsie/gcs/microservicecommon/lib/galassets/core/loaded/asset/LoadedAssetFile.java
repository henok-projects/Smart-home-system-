package com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetDataType;
import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetVersioningType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public abstract class LoadedAssetFile {

    /**
     * A LoadedDirectory must only have files. If it has folders, they are ignored.
     * The version of a LoadedDirectory is the string built from the versions of all the directories behind it.
     * That works because if any of the versions behind change, the version of this will change.
     */
    private String version;


    public abstract <T> T toDTO(Class<T> dtoType) throws Exception;

    public abstract String getBase64EncodedData(boolean includeFileVersion);

    /**
     * Constructs the version from the relative path of the file (relative to the path AssetType). It finds all the versions after the @ and groups them
     * @param path
     * @return
     */
    public static String getVersionFromPath(String path){
        return Arrays.stream(path.split("/")).map(dirName -> dirName.split("@")).filter(split -> split.length == 2).map(split -> split[1]).collect(Collectors.joining("."));
    }

    /*
    If the AssetVersioningType is FILE_VERSIONED, the version key name is AssetVersioningType.FILE_VERSION_KEY_NAME
     */
    public static LoadedAssetFile fromRawStringData(String path, String data, AssetDataType dataType, AssetVersioningType versioningType) throws Exception{
        return switch (dataType) {
            case YML -> LoadedYmlAssetFile.fromRawStringData(path, data, versioningType);
            case XML -> LoadedXmlAssetFile.fromRawStringData(path, data, versioningType);
            case PNG -> LoadedPngAssetFile.fromRawStringData(path, data, versioningType);
            case GIF -> LoadedGifAssetFile.fromRawStringData(path, data, versioningType);
            case JSON -> LoadedJsonAssetFile.fromRawStringData(path, data, versioningType);
            case TTF -> LoadedTtfAssetFile.fromRawStringData(path, data, versioningType);
        };
    }

    public static LoadedAssetFile fromInputStream(String path, InputStream inputStream, AssetDataType dataType, AssetVersioningType versioningType) throws Exception {
        return switch (dataType) {
            case YML -> LoadedYmlAssetFile.fromInputStream(path, inputStream, versioningType);
            case XML -> LoadedXmlAssetFile.fromInputStream(path, inputStream, versioningType);
            case PNG -> LoadedPngAssetFile.fromInputStream(path, inputStream, versioningType);
            case GIF -> LoadedGifAssetFile.fromInputStream(path, inputStream, versioningType);
            case JSON -> LoadedJsonAssetFile.fromInputStream(path, inputStream, versioningType);
            case TTF -> LoadedTtfAssetFile.fromInputStream(path, inputStream, versioningType);
        };
    }

}
