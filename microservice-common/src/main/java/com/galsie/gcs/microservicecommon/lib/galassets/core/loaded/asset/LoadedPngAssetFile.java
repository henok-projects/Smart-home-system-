package com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetVersioningType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LoadedPngAssetFile extends LoadedImageAssetFile {

    public LoadedPngAssetFile(String version, byte[] imageData) {
        super(version, imageData);
    }

    public static LoadedPngAssetFile fromRawStringData(String path, String data, AssetVersioningType versioningType) throws Exception{
        return fromInputStream(path, new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)), versioningType);
    }
    public static LoadedPngAssetFile fromInputStream(String path, InputStream stream, AssetVersioningType type) throws  Exception{
        String version = switch (type) {
            case FILE_VERSIONED -> throw new Exception("Couldn't create new instance: File versioning not supported for png files");
            case DIRECTORY_VERSIONED -> LoadedAssetFile.getVersionFromPath(path);
        };
        if (version == null){
            throw new Exception("Couldn't create new instance: Version couldn't be loaded for path " + path);
        }
        return new LoadedPngAssetFile(version, stream.readAllBytes());
    }

}
