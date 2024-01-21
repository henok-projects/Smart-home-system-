package com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetVersioningType;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.util.Arrays;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Getter
@Setter
public class LoadedYmlAssetFile extends LoadedAssetFile {
    private static Yaml yaml = new Yaml();
    private HashMap<String, Object> ymlData;

    public LoadedYmlAssetFile(String version, HashMap<String, Object> ymlData){
        super(version);
        this.ymlData = ymlData;
    }

    @Override
    public <T> T toDTO(Class<T> dtoType) throws Exception {
        throw new Exception("Yml files can't be mapped to DTOs");
    }

    @Override
    public String getBase64EncodedData(boolean includeFileVersion){
        Map<String, Object> newMap = new HashMap<>(ymlData);
        newMap.put("assets_file_version", getVersion());
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        var yaml = new Yaml(options);
        var yamlString = yaml.dump(newMap);
        if (includeFileVersion) return Base64.encodeBase64String(Arrays.concatenate(getVersion().getBytes(), yamlString.getBytes()));
        return Base64.encodeBase64String(yamlString.getBytes());
    }

    /**
     * Raw data means the version wasn't extracted from the data yet. Scales to any other data that describes the file
     * @return
     */
    private static LoadedYmlAssetFile fromRawYmlData(String path, HashMap<String, Object> ymlData, AssetVersioningType versioningType) throws Exception{

        String version = switch (versioningType) {
            case FILE_VERSIONED -> Objects.requireNonNull((String) ymlData.remove(AssetVersioningType.FILE_VERSION_KEY_NAME));
            case DIRECTORY_VERSIONED -> getVersionFromPath(path);
        };
        if (version == null){
            throw new Exception("Couldn't create new instance: Failed to load version");
        }
        return new LoadedYmlAssetFile(version, ymlData);
    }

    public static LoadedYmlAssetFile fromRawStringData(String path, String ymlData, AssetVersioningType versioningType) throws Exception{
        return fromRawYmlData(path, yaml.load(ymlData), versioningType);
    }

    public static LoadedYmlAssetFile fromInputStream(String path, InputStream is, AssetVersioningType versioningType) throws Exception{
        return fromRawYmlData(path, yaml.load(is), versioningType);
    }

}
