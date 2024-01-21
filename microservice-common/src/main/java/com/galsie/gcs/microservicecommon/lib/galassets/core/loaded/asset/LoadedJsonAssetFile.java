package com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetVersioningType;
import lombok.Getter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.json.JsonParserFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
File version key name is 'assets_file_version'
 */
@Getter
public class LoadedJsonAssetFile extends LoadedAssetFile {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Object> jsonData;

    public LoadedJsonAssetFile(String version, Map<String, Object> jsonData){
        super(version);
        this.jsonData = jsonData;
    }


    @Override
    public <T> T toDTO(Class<T> dtoType) {
        var newMap = new HashMap<>(jsonData);
        newMap.put("assets_file_version", getVersion());
        return objectMapper.convertValue(newMap, dtoType);
    }

    @Override
    public String getBase64EncodedData(boolean includeFileVersion) {
        Map<String, Object> newMap = new HashMap<>(jsonData);
        if (includeFileVersion) {
            newMap.put("assets_file_version", getVersion());
        }
        var jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(newMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Base64.encodeBase64String(jsonString.getBytes());
    }

    /*
    If file versioned, removes the
     */
    private static LoadedJsonAssetFile fromParsedJsonData(String path, Map<String, Object> jsonData, AssetVersioningType versioningType) throws Exception {
        String version = switch (versioningType) {
            case FILE_VERSIONED -> Objects.requireNonNull((String) jsonData.remove(AssetVersioningType.FILE_VERSION_KEY_NAME)); // throw null pointer exception if not found
            case DIRECTORY_VERSIONED -> LoadedAssetFile.getVersionFromPath(path);
        };
        if (version == null){
            throw new Exception("Couldn't create new instance: Failed to load version for file '" + path + "' with versioning type " + versioningType);
        }
        return new LoadedJsonAssetFile(version, jsonData);

    }

    public static LoadedJsonAssetFile fromRawStringData(String path, String jsonData, AssetVersioningType versioningType) throws Exception {
        return LoadedJsonAssetFile.fromParsedJsonData(path, JsonParserFactory.getJsonParser().parseMap(jsonData), versioningType);
    }
    public static LoadedJsonAssetFile fromInputStream(String path, InputStream is, AssetVersioningType versioningType) throws Exception{
        return LoadedJsonAssetFile.fromRawStringData(path, new String(is.readAllBytes()), versioningType);
    }

}
