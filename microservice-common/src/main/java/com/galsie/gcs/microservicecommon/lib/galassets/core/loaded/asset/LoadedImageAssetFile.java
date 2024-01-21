package com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset;

import lombok.Getter;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;

/**
 * The parent class for all loaded image asset files like {@link LoadedPngAssetFile} and {@link LoadedGifAssetFile}
 */
@Getter
public abstract class LoadedImageAssetFile extends LoadedAssetFile{

    private final byte[] imageData;

    public LoadedImageAssetFile(String version, byte[] imageData) {
        super(version);
        this.imageData = imageData;
    }


    @Override
    public <T> T toDTO(Class<T> dto) throws Exception {
        throw new Exception(this.getClass() + " files can't be mapped to DTOs");
    }

    @Override
    public String getBase64EncodedData(boolean includeVersion) {
        if(includeVersion) return Base64.toBase64String(Arrays.concatenate(getVersion().getBytes(), imageData));
        return Base64.toBase64String(imageData);
    }

}
