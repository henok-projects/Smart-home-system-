package com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetVersioningType;
import lombok.Getter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/*
File versioning is not supported
 */
@Getter
public class LoadedTtfAssetFile extends LoadedAssetFile {
    private final byte[] fontData;
    public LoadedTtfAssetFile(String version, byte[] fontData) {
        super(version);
        this.fontData = fontData;
    }

    @Override
    public <T> T toDTO(Class<T> dtoType) throws Exception {
        throw new Exception("TTF files can't be mapped to DTOs");
    }

    @Override
    public String getBase64EncodedData(boolean includeFileVersion) {
        if(includeFileVersion) return Base64.toBase64String(Arrays.concatenate(getVersion().getBytes(),fontData));
        return Base64.toBase64String(fontData);
    }

    public static LoadedTtfAssetFile fromRawStringData(String path, String data, AssetVersioningType versioningType) throws Exception{
        return fromInputStream(path, new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)), versioningType);
    }
    public static LoadedTtfAssetFile fromInputStream(String path, InputStream stream, AssetVersioningType type) throws  Exception{
        String version = switch (
                type) {
            case FILE_VERSIONED -> throw new Exception("Couldn't create new instance: File versioning not supported for TTF files");
            case DIRECTORY_VERSIONED -> LoadedAssetFile.getVersionFromPath(path);
        };
        if (version == null){
            throw new Exception("Couldn't create new instance: Version couldn't be loaded for path " + path);
        }
        return new LoadedTtfAssetFile(version, stream.readAllBytes());

    }

}
